package pl.edu.wat.wcy.epistimi.organization

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

internal class OrganizationServiceTest : ShouldSpec({
    val organizationRepository = mockk<OrganizationRepository>()
    val userRepository = mockk<UserRepository>()
    val locationClient = mockk<OrganizationLocationClient>()

    val organizationService = OrganizationService(organizationRepository, userRepository, locationClient)

    val userStub = { role: User.Role ->
        User(
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
            every { userRepository.findById("admin_id") } returns userStub(role)
            every { userRepository.findById("director_id") } returns userStub(TEACHER)

            // when & then
            shouldThrow<AdministratorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", "admin_id", "director_id", addressStub)
                )
            }
        }
    }

    forAll(
        row(EPISTIMI_ADMIN),
        row(ORGANIZATION_ADMIN),
    ) { role ->
        should("successfully register a new organization if provided admin has $role role") {
            // given
            every { userRepository.findById("admin_id") } returns userStub(role)
            every { userRepository.findById("director_id") } returns userStub(TEACHER)
            every { organizationRepository.save(any()) } returnsArgument 0
            every { locationClient.getLocation(any()) } returns null

            // when & then
            shouldNotThrow<AdministratorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", "admin_id", "director_id", addressStub)
                )
            }
        }
    }

    should("fail to register a new organization if admin with provided id does not exist") {
        // given
        every { userRepository.findById("admin_id") } throws UserNotFoundException()
        every { userRepository.findById("director_id") } returns userStub(TEACHER)

        // when & then
        shouldThrow<AdministratorNotFoundException> {
            organizationService.registerOrganization(
                OrganizationRegisterRequest("ABC", "admin_id", "director_id", addressStub)
            )
        }
    }

    forAll(
        row(STUDENT),
        row(PARENT),
    ) { role ->
        should("fail to register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById("admin_id") } returns userStub(ORGANIZATION_ADMIN)
            every { userRepository.findById("director_id") } returns userStub(role)

            // when & then
            shouldThrow<DirectorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", "admin_id", "director_id", addressStub)
                )
            }
        }
    }

    forAll(
        row(EPISTIMI_ADMIN),
        row(ORGANIZATION_ADMIN),
        row(TEACHER),
    ) { role ->
        should("successfully register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById("admin_id") } returns userStub(ORGANIZATION_ADMIN)
            every { userRepository.findById("director_id") } returns userStub(role)
            every { locationClient.getLocation(any()) } returns null

            // when & then
            shouldNotThrow<DirectorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", "admin_id", "director_id", addressStub)
                )
            }
        }
    }


    should("fail to register a new organization if provided director with provided id does not exist") {
        // given
        every { userRepository.findById("admin_id") } returns userStub(ORGANIZATION_ADMIN)
        every { userRepository.findById("director_id") } throws UserNotFoundException()

        // when & then
        shouldThrow<DirectorNotFoundException> {
            organizationService.registerOrganization(
                OrganizationRegisterRequest("ABC", "admin_id", "director_id", addressStub)
            )
        }
    }
})
