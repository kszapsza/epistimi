package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRepository

internal class OrganizationContextProviderTest : ShouldSpec({

    val organizationRepository = mockk<OrganizationRepository>()
    val userRepository = mockk<UserRepository>()
    val teacherRepository = mockk<TeacherRepository>()

    val organizationContextProvider = OrganizationContextProvider(
        organizationRepository,
        userRepository,
        teacherRepository,
    )

    val organizationAdminStub = User(
        id = UserId("organization_admin_id"),
        firstName = "Jan",
        lastName = "Kowalski",
        role = ORGANIZATION_ADMIN,
        username = "username",
        passwordHash = "123",
        sex = MALE,
    )

    val teacherUserStub = User(
        id = UserId("teacher_user_id"),
        firstName = "Adrianna",
        lastName = "Nowak",
        role = TEACHER,
        username = "teacher",
        passwordHash = "123",
        sex = FEMALE,
    )

    val organizationStub = Organization(
        id = OrganizationId("organization_id"),
        name = "SP7",
        admin = organizationAdminStub,
        status = ENABLED,
        director = organizationAdminStub,
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
        user = teacherUserStub,
        organization = organizationStub,
        academicTitle = null,
    )

    should("provide an organization administered by provided ORGANIZATION_ADMIN") {
        // given
        every { userRepository.findById(UserId("organization_admin_id")) } returns organizationAdminStub
        every { organizationRepository.findFirstByAdminId(UserId("organization_admin_id")) } returns organizationStub

        // when
        val organization = organizationContextProvider.provide(UserId("organization_admin_id"))

        // then
        organization shouldBe organizationStub
    }

    should("provide an organization connected with provided TEACHER") {
        // given
        every { userRepository.findById(UserId("teacher_user_id")) } returns teacherUserStub
        every { teacherRepository.findByUserId(UserId("teacher_user_id")) } returns teacherStub
        every { organizationRepository.findById(OrganizationId("organization_id")) } returns organizationStub

        // when
        val organization = organizationContextProvider.provide(UserId("teacher_user_id"))

        // then
        organization shouldBe organizationStub
    }

})
