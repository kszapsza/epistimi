package pl.edu.wat.wcy.epistimi.course

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseBadRequestException
import pl.edu.wat.wcy.epistimi.course.domain.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.domain.port.CourseRepository
import pl.edu.wat.wcy.epistimi.course.domain.service.CourseRegistrationService
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import java.time.LocalDate
import java.util.UUID

internal class CourseRegistrarTest : ShouldSpec({
    val courseRepository = mockk<CourseRepository>()
    val teacherRepository = mockk<TeacherRepository>()

    val courseRegistrationService = CourseRegistrationService(
        courseRepository,
        teacherRepository,
    )

    val contextOrganization = TestData.organization()

    val teacher = TestData.teacher(
        user = TestData.Users.teacher(
            organization = contextOrganization,
        ),
    )

    val validCourseCreateRequest = CourseCreateRequest(
        codeNumber = 1,
        codeLetter = "b",
        schoolYearBegin = LocalDate.of(2010, 9, 1),
        schoolYearSemesterEnd = LocalDate.of(2011, 2, 1),
        schoolYearEnd = LocalDate.of(2011, 6, 30),
        classTeacherId = teacher.id!!,
        profile = null,
        profession = null,
        specialization = null,
    )

    should("throw an exception when registering new course if semester end date is before school year begin date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrationService.createCourse(
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2010, 7, 1),
                    schoolYearEnd = LocalDate.of(2011, 6, 30),
                ),
                contextOrganization = contextOrganization,
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year end date is before school year begin date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrationService.createCourse(
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2011, 2, 10),
                    schoolYearEnd = LocalDate.of(2009, 6, 30),
                ),
                contextOrganization = contextOrganization,
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year end date is before semester end date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrationService.createCourse(
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2011, 2, 10),
                    schoolYearEnd = LocalDate.of(2011, 1, 28),
                ),
                contextOrganization = contextOrganization,
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year doesn't end in next calendar year after the beginning") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrationService.createCourse(
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2010, 10, 1),
                    schoolYearEnd = LocalDate.of(2010, 11, 1),
                ),
                contextOrganization = contextOrganization,
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if provided teacher is connected with other organization") {
        // given
        val teacherFromOtherOrganizationId = TeacherId(UUID.randomUUID())

        every { teacherRepository.findById(teacherFromOtherOrganizationId) } returns Teacher(
            id = teacherFromOtherOrganizationId,
            user = TestData.Users.teacher(
                id = UserId(UUID.randomUUID()),
                organization = TestData.organization(id = OrganizationId(UUID.randomUUID()))
            ),
            academicTitle = null,
        )

        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrationService.createCourse(
                createRequest = validCourseCreateRequest.copy(
                    classTeacherId = teacherFromOtherOrganizationId,
                ),
                contextOrganization = contextOrganization,
            )
        }

        // then
        exception.message shouldBe "Provided class teacher is not associated with your organization"
    }

    should("successfully register new course") {
        // given
        every { teacherRepository.findById(teacher.id!!) } returns teacher
        every { courseRepository.save(any()) } returnsArgument 0

        // when
        val newCourse = courseRegistrationService.createCourse(
            createRequest = validCourseCreateRequest,
            contextOrganization = contextOrganization,
        )

        // then
        newCourse shouldBeEqualToComparingFields Course(
            id = null,
            organization = contextOrganization,
            codeNumber = 1,
            codeLetter = "b",
            classTeacher = teacher,
            students = emptySet(),
            schoolYearBegin = LocalDate.of(2010, 9, 1),
            schoolYearSemesterEnd = LocalDate.of(2011, 2, 1),
            schoolYearEnd = LocalDate.of(2011, 6, 30),
            profile = null,
            profession = null,
            specialization = null,
            subjects = emptySet(),
        )
    }
})
