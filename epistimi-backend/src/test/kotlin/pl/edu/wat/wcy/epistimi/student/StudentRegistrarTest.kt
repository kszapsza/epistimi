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
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.ParentDetails
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar.NewParent
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser

internal class StudentRegistrarTest : ShouldSpec({

    val studentRepository = mockk<StudentRepository>()
    val userRegistrar = mockk<UserRegistrar>()
    val parentRegistrar = mockk<ParentRegistrar>()
    val courseFacade = mockk<CourseFacade>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val studentRegistrar = StudentRegistrar(
        studentRepository,
        userRegistrar,
        parentRegistrar,
        courseFacade,
        organizationContextProvider,
    )

    val userRegisterRequest = UserRegisterRequest(
        firstName = "Jan",
        lastName = "Kowalski",
        role = STUDENT,
        address = TestData.address,
    )

    val courseStub = Course(
        id = CourseId("course_id"),
        organizationId = OrganizationId("organization_id"),
        code = Course.Code(
            number = "6",
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacherId = TeacherId("teacher_id"),
        studentIds = mutableListOf(),
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
            parentRegistrar.registerParents(
                requesterUserId = UserId("organization_admin_user_id"),
                userRegisterRequests = any()
            )
        } answers {
            with(secondArg<List<UserRegisterRequest>>()[0]) {
                listOf(
                    NewParent(
                        parent = ParentDetails(
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
                )
            }
        }
    }

    fun stubStudentRepository() {
        every { studentRepository.save(ofType(Student::class)) } answers {
            firstArg<Student>().copy(id = StudentId("student_id"))
        }
    }

    fun stubCourseService() {
        every {
            courseFacade.addStudent(
                courseId = CourseId("course_id"),
                studentId = StudentId("student_id"),
            )
        } returns courseStub
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
            parent = ParentDetails(
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
        verify {
            parentRegistrar.registerParents(
                requesterUserId = UserId("organization_admin_user_id"),
                userRegisterRequests = listOf(parentUserRegisterRequest),
            )
        }
        verify { studentRepository.save(ofType(Student::class)) }
        verify { courseFacade.addStudent(CourseId("course_id"), StudentId("student_id")) }
    }
})
