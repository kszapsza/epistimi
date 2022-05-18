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
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetails
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import java.time.LocalDate

internal class CourseFacadeTest : ShouldSpec({
    val courseRepository = mockk<CourseRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()
    val courseDetailsDecorator = mockk<CourseDetailsDecorator>()

    val courseFacade = CourseFacade(
        courseRepository,
        teacherRepository,
        organizationContextProvider,
        courseDetailsDecorator
    )

    val teacherStub = Teacher(
        id = TeacherId("teacher_id"),
        userId = TestData.Users.teacher.id!!,
        organizationId = TestData.organization.id!!,
        academicTitle = "dr",
    )

    val courseStub = Course(
        id = CourseId("course1"),
        organizationId = TestData.organization.id!!,
        code = Course.Code(
            number = "6",
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacherId = teacherStub.id!!,
        studentIds = emptyList(),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
        profession = null,
        profile = null,
        specialization = null,
    )

    val courseDetailsStub = CourseDetails(
        id = CourseId("course1"),
        organization = TestData.organization,
        code = Course.Code(
            number = "6",
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacher = TeacherDetails(
            id = TeacherId("teacher_id"),
            user = TestData.Users.teacher,
            organization = TestData.organization,
            academicTitle = "dr",
        ),
        students = emptyList(),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
        profession = null,
        profile = null,
        specialization = null,
    )

    val studentStub = Student(
        id = StudentId("student_id"),
        userId = UserId("student_user_id"),
        organizationId = TestData.organization.id!!,
        parentsIds = emptyList(),
    )

    should("return list of courses for organization administered by admin with provided id") {
        // given
        every { courseRepository.findAll(OrganizationId("organization_id")) } returns listOf(courseStub)
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization
        every { courseDetailsDecorator.decorate(courseStub) } returns courseDetailsStub

        // when
        val courses = courseFacade.getCourses(UserId("admin_user_id"))

        // then
        with(courses) {
            shouldHaveSize(1)
            shouldContain(courseDetailsStub)
        }

        // and
        verify { courseDetailsDecorator.decorate(courseStub) }
    }

    should("return empty list of courses if admin with provided id does not administer any organization") {
        // given
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns null

        // when
        val courses = courseFacade.getCourses(UserId("admin_user_id"))

        // then
        courses.shouldBeEmpty()
    }

    should("return a single course by id") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization
        every { courseDetailsDecorator.decorate(courseStub) } returns courseDetailsStub

        // when
        val course = courseFacade.getCourse(CourseId("course_id"), UserId("admin_user_id"))

        // then
        course shouldBe courseDetailsStub

        // and
        verify { courseDetailsDecorator.decorate(courseStub) }
    }

    should("throw an exception if course with provided exists in organization not managed by current user") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationContextProvider.provide(UserId("other_admin_id")) } returns null

        // expect
        shouldThrow<CourseNotFoundException> {
            courseFacade.getCourse(CourseId("course_id"), UserId("other_admin_id"))
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
            courseFacade.createCourse(
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
            courseFacade.createCourse(
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
            courseFacade.createCourse(
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
            courseFacade.createCourse(
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
            userId = UserId("other_teacher_user_id"),
            organizationId = OrganizationId("other_organization_id"),
            academicTitle = null,
        )

        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseFacade.createCourse(
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
        every { courseDetailsDecorator.decorate(ofType(Course::class)) } returns courseDetailsStub

        // expect
        shouldNotThrow<CourseBadRequestException> {
            courseFacade.createCourse(
                userId = UserId("user_id"),
                createRequest = validCourseCreateRequest,
            )
        }

        // and
        verify {
            courseRepository.save(
                Course(
                    id = null,
                    organizationId = TestData.organization.id!!,
                    code = Course.Code(
                        number = "1",
                        letter = "b"
                    ),
                    schoolYear = "2010/2011",
                    classTeacherId = teacherStub.id!!,
                    studentIds = emptyList(),
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
        every { courseRepository.findById(CourseId("course_id")) } returns
                courseStub.copy(schoolYearEnd = LocalDate.now().plusMonths(1))
        every { courseRepository.save(any()) } returnsArgument 0

        // when
        val updatedCourse = courseFacade.addStudent(CourseId("course_id"), studentStub.id!!)

        // then
        with(updatedCourse.studentIds) {
            shouldHaveSize(1)
            shouldContain(studentStub.id!!)
        }
    }

    should("fail adding new student to existing course if it has already ended") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns
                courseStub.copy(schoolYearEnd = LocalDate.now().minusMonths(3))

        // expect
        shouldThrow<CourseUnmodifiableException> {
            courseFacade.addStudent(CourseId("course_id"), studentStub.id!!)
        }
    }
})
