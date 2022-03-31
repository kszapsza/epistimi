package pl.edu.wat.wcy.epistimi.course

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
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
import pl.edu.wat.wcy.epistimi.user.UserRepository

internal class CourseServiceTest : ShouldSpec({
    val courseRepository = mockk<CourseRepository>()
    val userRepository = mockk<UserRepository>()
    val organizationRepository = mockk<OrganizationRepository>()

    val courseService = CourseService(courseRepository, userRepository, organizationRepository)

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

    val courseStub = Course(
        id = CourseId("course1"),
        organization = organizationStub,
        code = Course.Code(
            number = 6,
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacher = teacherStub,
        students = listOf(),
    )

    should("return list of courses for organization administered by admin with provided id") {
        // given
        every { userRepository.findById(UserId("admin_user_id")) } returns userStub
        every { organizationRepository.findAllByAdminId(UserId("admin_user_id")) } returns listOf(organizationStub)
        every { courseRepository.findAll(OrganizationId("organization_id")) } returns listOf(courseStub)

        // when
        val courses = courseService.getCourses(UserId("admin_user_id"))

        // then
        courses shouldHaveSize 1
        courses shouldContain courseStub
    }

    should("return empty list of courses if admin with provided id does not administer any organization") {
        // given
        every { userRepository.findById(UserId("admin_user_id")) } returns userStub
        every { organizationRepository.findAllByAdminId(UserId("admin_user_id")) } returns listOf()

        // when
        val courses = courseService.getCourses(UserId("admin_user_id"))

        // then
        courses.shouldBeEmpty()
    }
})
