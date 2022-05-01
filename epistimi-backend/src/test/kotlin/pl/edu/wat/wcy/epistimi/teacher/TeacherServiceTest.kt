package pl.edu.wat.wcy.epistimi.teacher

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
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

internal class TeacherServiceTest : ShouldSpec({

    val teacherRepository = mockk<TeacherRepository>()
    val organizationRepository = mockk<OrganizationRepository>()

    val teacherService = TeacherService(teacherRepository, organizationRepository)

    val userStub = User(
        id = UserId("admin_user_id"),
        firstName = "Jan",
        lastName = "Kowalski",
        role = User.Role.ORGANIZATION_ADMIN,
        username = "j.kowalski",
        passwordHash = "123",
        sex = User.Sex.MALE,
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
        user = userStub,
        organization = organizationStub,
        academicTitle = null,
    )

    should("return empty list of teachers if there are no teachers in school administered by provided admin") {
        // given
        every { organizationRepository.findFirstByAdminId(UserId("admin_user_id")) } returns organizationStub
        every { teacherRepository.findAll(OrganizationId("organization_id")) } returns listOf()

        // when
        val teachers = teacherService.getTeachers(UserId("admin_user_id"))

        // then
        teachers.shouldBeEmpty()
    }

    should("return list of teachers in school administered by provided admin") {
        // given
        every { organizationRepository.findFirstByAdminId(UserId("admin_user_id")) } returns organizationStub
        every { teacherRepository.findAll(OrganizationId("organization_id")) } returns listOf(teacherStub)

        // when
        val teachers = teacherService.getTeachers(UserId("admin_user_id"))

        // then
        with(teachers) {
            shouldHaveSize(1)
            shouldContain(teacherStub)
        }
    }

})
