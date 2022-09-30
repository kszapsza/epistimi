package pl.edu.wat.wcy.epistimi.course

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import java.time.LocalDate
import java.util.UUID

internal class CourseRegistrarTest : ShouldSpec({
    val courseRepository = mockk<CourseRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val courseRegistrar = CourseRegistrar(
        courseRepository,
        teacherRepository,
        organizationContextProvider,
    )

    val validCourseCreateRequest = CourseCreateRequest(
        codeNumber = 1,
        codeLetter = "b",
        schoolYearBegin = LocalDate.of(2010, 9, 1),
        schoolYearSemesterEnd = LocalDate.of(2011, 2, 1),
        schoolYearEnd = LocalDate.of(2011, 6, 30),
        classTeacherId = TestData.teacher.id!!,
        profile = null,
        profession = null,
        specialization = null,
    )

    should("throw an exception when registering new course if semester end date is before school year begin date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrar.createCourse(
                userId = UserId(UUID.randomUUID()),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2010, 7, 1),
                    schoolYearEnd = LocalDate.of(2011, 6, 30),
                ),
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year end date is before school year begin date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrar.createCourse(
                userId = UserId(UUID.randomUUID()),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2011, 2, 10),
                    schoolYearEnd = LocalDate.of(2009, 6, 30),
                ),
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year end date is before semester end date") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrar.createCourse(
                userId = UserId(UUID.randomUUID()),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2011, 2, 10),
                    schoolYearEnd = LocalDate.of(2011, 1, 28),
                ),
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if school year doesn't end in next calendar year after the beginning") {
        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrar.createCourse(
                userId = UserId(UUID.randomUUID()),
                createRequest = validCourseCreateRequest.copy(
                    schoolYearBegin = LocalDate.of(2010, 9, 1),
                    schoolYearSemesterEnd = LocalDate.of(2010, 10, 1),
                    schoolYearEnd = LocalDate.of(2010, 11, 1),
                ),
            )
        }

        // then
        exception.message shouldBe "Invalid school year time frame"
    }

    should("throw an exception when registering new course if provided teacher is connected with other organization") {
        // given
        val userContextId = TestData.organization.admin.id!!
        val teacherFromOtherOrganizationId = TeacherId(UUID.randomUUID())

        every { organizationContextProvider.provide(userContextId) } returns TestData.organization
        every { teacherRepository.findById(teacherFromOtherOrganizationId) } returns Teacher(
            id = teacherFromOtherOrganizationId,
            user = TestData.Users.teacher.copy(id = UserId(UUID.randomUUID())),
            organization = TestData.organization.copy(id = OrganizationId(UUID.randomUUID())),
            academicTitle = null,
        )

        // when
        val exception = shouldThrow<CourseBadRequestException> {
            courseRegistrar.createCourse(
                userId = userContextId,
                createRequest = validCourseCreateRequest.copy(
                    classTeacherId = teacherFromOtherOrganizationId,
                ),
            )
        }

        // then
        exception.message shouldBe "Provided class teacher is not associated with your organization"
    }

    should("successfully register new course") {
        // given
        val userContextId = TestData.organization.admin.id!!

        every { organizationContextProvider.provide(userContextId) } returns TestData.organization
        every { teacherRepository.findById(TestData.teacher.id!!) } returns TestData.teacher
        every { courseRepository.save(any()) } returnsArgument 0

        // expect
        shouldNotThrow<CourseBadRequestException> {
            courseRegistrar.createCourse(
                userId = userContextId,
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
                        number = 1,
                        letter = "b"
                    ),
                    schoolYear = "2010/2011",
                    classTeacher = TestData.teacher,
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
})
