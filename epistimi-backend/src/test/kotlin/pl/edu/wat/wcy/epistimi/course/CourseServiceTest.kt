package pl.edu.wat.wcy.epistimi.course

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.TestUtils
import pl.edu.wat.wcy.epistimi.course.dto.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import java.time.LocalDate

internal class CourseServiceTest : ShouldSpec({
    val courseRepository = mockk<CourseRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val courseService = CourseService(courseRepository, teacherRepository, organizationContextProvider)

    val teacherStub = Teacher(
        id = TeacherId("teacher_id"),
        user = User(
            id = UserId("teacher_user_id"),
            firstName = "Józefa",
            lastName = "Nowak",
            role = TEACHER,
            username = "j.nowak",
            passwordHash = "123",
            sex = FEMALE,
        ),
        organization = TestData.organization,
        academicTitle = "dr",
    )

    val courseStub = Course(
        id = CourseId("course1"),
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

    val studentStub = Student(
        id = StudentId("student_id"),
        user = User(
            id = UserId("student_user_id"),
            firstName = "Adrian",
            lastName = "Kowalski",
            role = STUDENT,
            username = "a.kowalski",
            passwordHash = "123",
            sex = MALE,
        ),
        organization = TestData.organization,
        parents = emptyList(),
    )

    should("return list of courses for organization administered by admin with provided id") {
        // given
        every { courseRepository.findAll(OrganizationId("organization_id")) } returns listOf(courseStub)
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization

        // when
        val courses = courseService.getCourses(UserId("admin_user_id"))

        // then
        with(courses) {
            shouldHaveSize(1)
            shouldContain(courseStub)
        }
    }

    should("return empty list of courses if admin with provided id does not administer any organization") {
        // given
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns null

        // when
        val courses = courseService.getCourses(UserId("admin_user_id"))

        // then
        courses.shouldBeEmpty()
    }

    should("return a single course by id") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization

        // when
        val course = courseService.getCourse(CourseId("course_id"), UserId("admin_user_id"))

        // then
        course shouldBe courseStub
    }

    should("throw an exception if course with provided exists in organization not managed by current user") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationContextProvider.provide(UserId("other_admin_id")) } returns null

        // expect
        shouldThrow<CourseNotFoundException> {
            courseService.getCourse(CourseId("course_id"), UserId("other_admin_id"))
        }
    }

    val validCourseCreateRequest = CourseCreateRequest(
        codeNumber = 1,
        codeLetter = "b",
        schoolYearBegin = LocalDate.of(2010, 9, 1),
        schoolYearSemesterEnd = LocalDate.of(2011, 2, 1),
        schoolYearEnd = LocalDate.of(2011, 6, 30),
        classTeacherId = TeacherId("teacher_id"),
        profile = null,
        profession = null,
        specialization = null,
    )

    should("throw an exception when registering new course if semester end date is before school year begin date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseService.createCourse(
                userId = UserId("user_id"),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2010, 7, 1),
                    schoolYearEnd = LocalDate.of(2011, 6, 30),
                )
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year end date is before school year begin date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseService.createCourse(
                userId = UserId("user_id"),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2011, 2, 10),
                    schoolYearEnd = LocalDate.of(2009, 6, 30),
                )
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year end date is before semester end date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseService.createCourse(
                userId = UserId("user_id"),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2011, 2, 10),
                    schoolYearEnd = LocalDate.of(2011, 1, 28),
                )
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year doesn't end in next calendar year after the beginning") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseService.createCourse(
                userId = UserId("user_id"),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2010, 10, 1),
                    schoolYearEnd = LocalDate.of(2010, 11, 1),
                )
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if provided teacher is connected with other organization") {
        // given
        every { organizationContextProvider.provide(UserId("user_id")) } returns TestData.organization
        every { teacherRepository.findById(TeacherId("other_teacher_id")) } returns Teacher(
            id = TeacherId("other_teacher_id"),
            user = User(
                id = UserId("other_teacher_user_id"),
                firstName = "Zdzisława",
                lastName = "Ciok",
                role = TEACHER,
                username = "z.ciok",
                passwordHash = "123",
                sex = FEMALE,
            ),
            organization = Organization(
                id = OrganizationId("other_organization_id"),
                name = "Other Organization",
                admin = TestData.Users.organizationAdmin,
                status = Organization.Status.ENABLED,
                director = TestData.Users.organizationAdmin,
                address = TestData.address,
                location = null,
            ),
            academicTitle = null,
        )

        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseService.createCourse(
                userId = UserId("user_id"),
                createRequest = validCourseCreateRequest.copy(
                    classTeacherId = TeacherId("other_teacher_id")
                ),
            )
        }

        // then
        exception.message shouldBe "Provided class teacher is not associated with your organization"
    }

    should("successfully register new course") {
        // given
        every { organizationContextProvider.provide(UserId("user_id")) } returns TestData.organization
        every { teacherRepository.findById(TeacherId("teacher_id")) } returns teacherStub
        every { courseRepository.save(any()) } returnsArgument 0

        // expect
        shouldNotThrow<CourseBadRequestException> {
            courseService.createCourse(
                userId = UserId("user_id"),
                createRequest = validCourseCreateRequest,
            )
        }

        // and
        verify {
            courseRepository.save(
                Course(
                    id = null,
                    organization = TestData.organization,
                    code = Course.Code(
                        number = "1",
                        letter = "b"
                    ),
                    schoolYear = "2010/2011",
                    classTeacher = teacherStub,
                    students = emptyList(),
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2011, 2, 1),
                    schoolYearEnd = LocalDate.of(2011, 6, 30),
                    profile = null,
                    profession = null,
                    specialization = null,
                )
            )
        }
    }

    should("add new student to existing course") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { courseRepository.save(any()) } returnsArgument 0

        // when
        val updatedCourse = courseService.addStudent(CourseId("course_id"), studentStub)

        // then
        with(updatedCourse.students) {
            shouldHaveSize(1)
            shouldContain(studentStub)
        }
    }
})
