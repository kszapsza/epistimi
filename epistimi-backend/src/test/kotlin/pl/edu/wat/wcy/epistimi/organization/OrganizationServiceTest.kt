package pl.edu.wat.wcy.epistimi.organization

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

class OrganizationServiceTest : ShouldSpec({
    val organizationRepository = mockk<OrganizationRepository>()
    val userRepository = mockk<UserRepository>()
    val organizationService = OrganizationService(organizationRepository, userRepository)

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

    forAll(
        row(STUDENT),
        row(PARENT),
        row(TEACHER),
    ) { role ->
        should("fail to register new organization if provided admin has $role role") {
            every { userRepository.findById("123") } returns userStub(role)

            shouldThrow<AdministratorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", "123")
                )
            }
        }
    }

    forAll(
        row(EPISTIMI_ADMIN),
        row(ORGANIZATION_ADMIN),
    ) { role ->
        should("successfully register new organization if provided admin has $role role") {
            every { userRepository.findById("123") } returns userStub(role)
            every { organizationRepository.save(any()) } returnsArgument 0

            shouldNotThrow<AdministratorInsufficientPermissionsException> {
                organizationService.registerOrganization(
                    OrganizationRegisterRequest("ABC", "123")
                )
            }
        }
    }

    should("fail to register new organization if admin with provided does not exist") {
        every { userRepository.findById("123") } throws UserNotFoundException()

        shouldThrow<AdministratorNotFoundException> {
            organizationService.registerOrganization(
                OrganizationRegisterRequest("ABC", "123")
            )
        }
    }
})
