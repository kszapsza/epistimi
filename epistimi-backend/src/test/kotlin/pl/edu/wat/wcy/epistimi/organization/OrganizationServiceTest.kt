package pl.edu.wat.wcy.epistimi.organization

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

internal class OrganizationServiceTest : ShouldSpec({
    val organizationRepository = mockk<OrganizationRepository>()
    val userRepository = mockk<UserRepository>()
    val locationClient = mockk<OrganizationLocationClient>()

    val organizationService = OrganizationService(organizationRepository, userRepository, locationClient)

    val userStub = { id: String, role: User.Role ->
        User(
            id = UserId(id),
            firstName = "Jan",
            lastName = "Kowalski",
            role = role,
            username = "j.kowalski",
            passwordHash = "123",
            sex = MALE,
        )
    }

    val addressStub = Address(
        street = "Szkolna 17",
        postalCode = "15-640",
        city = "Białystok",
        countryCode = "PL",
    )

    forAll(
        row(STUDENT),
        row(PARENT),
        row(TEACHER),
    ) { role ->
        should("fail to register a new organization if provided admin has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", role)
            every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", TEACHER)
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

            // when
            val exception = shouldThrow<AdminInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
                )
            }

            // then
            exception.message shouldBe "User requested for admin role has insufficient permissions"
        }
    }

    forAll(
        row(EPISTIMI_ADMIN),
        row(ORGANIZATION_ADMIN),
    ) { role ->
        should("successfully register a new organization if provided admin has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", role)
            every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", TEACHER)
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null
            every { organizationRepository.save(any()) } returnsArgument 0
            every { locationClient.getLocation(any()) } returns null

            // expect
            shouldNotThrow<AdminInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
                )
            }
        }
    }

    should("fail to register a new organization if admin with provided id does not exist") {
        // given
        every { userRepository.findById(UserId("admin_id")) } throws UserNotFoundException()
        every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", TEACHER)
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

        // when
        val exception = shouldThrow<AdminNotFoundException> {
            organizationService.registerOrganization(
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
            )
        }

        // then
        exception.message shouldBe "Admin with id admin_id does not exist"
    }

    forAll(
        row(STUDENT),
        row(PARENT),
    ) { role ->
        should("fail to register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", ORGANIZATION_ADMIN)
            every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", role)
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

            // when
            val exception = shouldThrow<DirectorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
                )
            }

            // then
            exception.message shouldBe "User requested for director role has insufficient permissions"
        }
    }

    forAll(
        row(EPISTIMI_ADMIN),
        row(ORGANIZATION_ADMIN),
        row(TEACHER),
    ) { role ->
        should("successfully register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", ORGANIZATION_ADMIN)
            every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", role)
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null
            every { locationClient.getLocation(any()) } returns null

            // expect
            shouldNotThrow<DirectorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
                )
            }
        }
    }

    should("fail to register a new organization if provided director with provided id does not exist") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", ORGANIZATION_ADMIN)
        every { userRepository.findById(UserId("director_id")) } throws UserNotFoundException()
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

        // when
        val exception = shouldThrow<DirectorNotFoundException> {
            organizationService.registerOrganization(
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
            )
        }

        // then
        exception.message shouldBe "Director with id director_id does not exist"
    }

    should("fail to register a new organization if provided admin already manages other organization") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", ORGANIZATION_ADMIN)
        every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", TEACHER)
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns
                Organization(
                    id = OrganizationId("123"),
                    name = "SP7",
                    admin = userStub("admin_id", ORGANIZATION_ADMIN),
                    status = ENABLED,
                    director = userStub("director_id", TEACHER),
                    address = addressStub,
                    location = null,
                )

        // when
        val exception = shouldThrow<AdminManagingOtherOrganizationException> {
            organizationService.registerOrganization(
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
            )
        }

        // then
        exception.message shouldBe "Provided admin is already managing other organization"
    }

    should("successfully update existing organization if admin was not changed") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", ORGANIZATION_ADMIN)
        every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", TEACHER)
        every { organizationRepository.update(any()) } returnsArgument 0
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns Organization(
            id = OrganizationId("123"),
            name = "SP7",
            admin = userStub("admin_id", ORGANIZATION_ADMIN),
            status = ENABLED,
            director = userStub("director_id", TEACHER),
            address = addressStub,
            location = null,
        )
        every { locationClient.getLocation(any()) } returns null

        // expect
        shouldNotThrow<AdminManagingOtherOrganizationException> {
            organizationService.updateOrganization(
                OrganizationId("123"),
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
            )
        }
    }

    should("successfully update existing organization if provided admin does not manage any organization yet") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", ORGANIZATION_ADMIN)
        every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", TEACHER)
        every { organizationRepository.update(any()) } returnsArgument 0
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null
        every { locationClient.getLocation(any()) } returns null

        // expect
        shouldNotThrow<AdminManagingOtherOrganizationException> {
            organizationService.updateOrganization(
                OrganizationId("123"),
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
            )
        }
    }

    should("fail updating existing organization, if provided new admin already manages other organization") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns userStub("admin_id", ORGANIZATION_ADMIN)
        every { userRepository.findById(UserId("director_id")) } returns userStub("director_id", TEACHER)
        every { organizationRepository.update(any()) } returnsArgument 0
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns Organization(
            id = OrganizationId("some_different_id"),
            name = "G2",
            admin = userStub("admin_id", ORGANIZATION_ADMIN),
            status = ENABLED,
            director = userStub("director_id", TEACHER),
            address = addressStub,
            location = null,
        )
        every { locationClient.getLocation(any()) } returns null

        // when
        val exception = shouldThrow<AdminManagingOtherOrganizationException> {
            organizationService.updateOrganization(
                OrganizationId("some_id"),
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), addressStub)
            )
        }

        // then
        exception.message shouldBe "Provided admin is already managing other organization"
    }
})
