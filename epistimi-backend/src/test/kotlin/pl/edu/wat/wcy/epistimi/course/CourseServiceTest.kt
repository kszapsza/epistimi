package pl.edu.wat.wcy.epistimi.course

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import java.text.SimpleDateFormat
import java.util.Locale

internal class CourseServiceTest : ShouldSpec({
    val courseRepository = mockk<CourseRepository>()
    val organizationRepository = mockk<OrganizationRepository>()

    val courseService = CourseService(courseRepository, organizationRepository)

    val userStub = User(
        id = UserId("admin_user_id"),
        firstName = "Jan",
        lastName = "Kowalski",
        role = ORGANIZATION_ADMIN,
        username = "j.kowalski",
        passwordHash = "123",
        sex = MALE,
    )

    val organizationStub = Organization(
        id = OrganizationId("organization_id"),
        name = "SP7",
        admin = userStub,
        status = Organization.Status.ENABLED,
        director = userStub,
        address = Address(
            street = "Szkolna 17",
            postalCode = "15-640",
            city = "Białystok",
            countryCode = "PL",
        ),
        location = null,
    )

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
        organization = organizationStub,
        academicTitle = "dr",
    )

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    val courseStub = Course(
        id = CourseId("course1"),
        organization = organizationStub,
        code = Course.Code(
            number = "6",
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacher = teacherStub,
        students = listOf(),
        schoolYearBegin = formatter.parse("2012-09-03"),
        schoolYearSemesterEnd = formatter.parse("2013-01-18"),
        schoolYearEnd = formatter.parse("2013-06-28"),
    )

    should("return list of courses for organization administered by admin with provided id") {
        // given
        every { courseRepository.findAll(OrganizationId("organization_id")) } returns listOf(courseStub)
        every { organizationRepository.findAllByAdminId(UserId("admin_user_id")) } returns listOf(organizationStub)

        // when
        val courses = courseService.getCourses(UserId("admin_user_id"))

        // then
        courses shouldHaveSize 1
        courses shouldContain courseStub
    }

    should("return empty list of courses if admin with provided id does not administer any organization") {
        // given
        every { organizationRepository.findAllByAdminId(UserId("admin_user_id")) } returns listOf()

        // when
        val courses = courseService.getCourses(UserId("admin_user_id"))

        // then
        courses.shouldBeEmpty()
    }

    should("return a single course by id") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationRepository.findAllByAdminId(UserId("admin_user_id")) } returns listOf(organizationStub)

        // when
        val course = courseService.getCourse(CourseId("course_id"), UserId("admin_user_id"))

        // then
        course shouldBe courseStub
    }

    should("throw an exception if course with provided exists in organization not managed by current user") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationRepository.findAllByAdminId(UserId("other_admin_id")) } returns listOf()

        // expect
        shouldThrow<CourseNotFoundException> {
            courseService.getCourse(CourseId("course_id"), UserId("other_admin_id"))
        }
    }
})
