package pl.edu.wat.wcy.epistimi.student

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.TestUtils
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseService
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar.NewParent
import pl.edu.wat.wcy.epistimi.student.dto.StudentRegisterRequest
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

internal class StudentRegistrarTest : ShouldSpec({

    val studentRepository = mockk<StudentRepository>()
    val userRegistrar = mockk<UserRegistrar>()
    val parentRegistrar = mockk<ParentRegistrar>()
    val courseService = mockk<CourseService>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val studentRegistrar = StudentRegistrar(
        studentRepository,
        userRegistrar,
        parentRegistrar,
        courseService,
        organizationContextProvider,
    )

    val userRegisterRequest = UserRegisterRequest(
        firstName = "Jan",
        lastName = "Kowalski",
        role = STUDENT,
        username = null,
        password = null,
        address = TestData.address,
    )

    val teacherStub = Teacher(
        id = TeacherId("teacher_id"),
        user = TestData.Users.organizationAdmin,
        organization = TestData.organization,
        academicTitle = null,
    )

    val courseStub = Course(
        id = CourseId("course_id"),
        organization = TestData.organization,
        code = Course.Code(
            number = "6",
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacher = teacherStub,
        students = emptyList(),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
    )

    fun stubOrganizationContextProvider() {
        every {
            organizationContextProvider.provide(UserId("organization_admin_user_id"))
        } returns TestData.organization
    }

    fun stubUserRegistrar() {
        every {
            userRegistrar.registerUser(ofType(UserRegisterRequest::class))
        } answers {
            with(firstArg<UserRegisterRequest>()) {
                NewUser(
                    user = User(
                        id = UserId("user_id"),
                        firstName = firstName,
                        lastName = lastName,
                        role = role,
                        username = "$firstName.$lastName".lowercase(),
                        address = address,
                        passwordHash = "654321"
                    ),
                    password = "123456",
                )
            }
        }
    }

    fun stubParentRegistrar() {
        every {
            parentRegistrar.registerParent(
                requesterUserId = UserId("organization_admin_user_id"),
                userRegisterRequest = ofType(UserRegisterRequest::class)
            )
        } answers {
            with(secondArg<UserRegisterRequest>()) {
                NewParent(
                    parent = Parent(
                        id = ParentId("parent_id"),
                        user = User(
                            id = UserId("user_id"),
                            firstName = firstName,
                            lastName = lastName,
                            role = PARENT,
                            username = "$firstName.$lastName".lowercase(),
                            address = address,
                            passwordHash = "654321",
                        ),
                        organization = TestData.organization,
                    ),
                    password = "123456",
                )
            }
        }
    }

    fun stubStudentRepository() {
        every { studentRepository.save(ofType(Student::class)) } answers {
            (firstArg<Student>()).copy(id = StudentId("student_id"))
        }
    }

    fun stubCourseService() {
        every {
            courseService.addStudent(
                courseId = CourseId("course_id"),
                student = ofType(Student::class),
            )
        } answers {
            courseStub.let { oldCourse ->
                oldCourse.copy(students = oldCourse.students + secondArg<Student>())
            }
        }
    }

    should("register new student with underlying user profile and parents") {
        // given
        stubOrganizationContextProvider()
        stubUserRegistrar()
        stubParentRegistrar()
        stubStudentRepository()
        stubCourseService()

        // when
        val studentUserRegisterRequest = userRegisterRequest.copy(role = STUDENT)
        val parentUserRegisterRequest = userRegisterRequest.copy(role = PARENT, firstName = "Malwina", lastName = "Kowalska", sex = FEMALE)

        val (id, user, parents) = studentRegistrar.registerStudent(
            requesterUserId = UserId("organization_admin_user_id"),
            request = StudentRegisterRequest(
                courseId = CourseId("course_id"),
                user = studentUserRegisterRequest,
                parents = listOf(parentUserRegisterRequest),
            ),
        )

        // then
        id shouldBe StudentId("student_id")
        user shouldBe NewUser(
            user = User(
                id = UserId("user_id"),
                firstName = "Jan",
                lastName = "Kowalski",
                role = STUDENT,
                username = "jan.kowalski",
                address = TestData.address,
                passwordHash = "654321",
            ),
            password = "123456",
        )
        parents shouldHaveSize 1
        parents[0] shouldBe NewParent(
            parent = Parent(
                id = ParentId("parent_id"),
                user = User(
                    id = UserId("user_id"),
                    firstName = "Malwina",
                    lastName = "Kowalska",
                    role = PARENT,
                    username = "malwina.kowalska",
                    address = TestData.address,
                    passwordHash = "654321",
                ),
                organization = TestData.organization,
            ),
            password = "123456",
        )

        // and
        verify { organizationContextProvider.provide(UserId("organization_admin_user_id")) }
        verify { userRegistrar.registerUser(studentUserRegisterRequest) }
        verify { parentRegistrar.registerParent(
            requesterUserId = UserId("organization_admin_user_id"),
            userRegisterRequest = parentUserRegisterRequest,
        ) }
        verify { studentRepository.save(ofType(Student::class)) }
        verify { courseService.addStudent(CourseId("course_id"), ofType(Student::class)) }
    }
})
