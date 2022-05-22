package pl.edu.wat.wcy.epistimi.organization

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRegistrar

class OrganizationRegistrarTest : ShouldSpec({
    val organizationRepository = mockk<OrganizationRepository>()
    val userRegistrar = mockk<UserRegistrar>()
    val locationClient = mockk<OrganizationLocationClient>()

    val organizationRegistrar = OrganizationRegistrar(
        organizationRepository,
        userRegistrar,
        locationClient,
    )

    forAll(
        row(User.Role.STUDENT),
        row(User.Role.PARENT),
        row(User.Role.TEACHER),
    ) { role ->
        should("fail to register a new organization if provided admin has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(role, "admin_id")
            every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(User.Role.TEACHER, "director_id")
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

            // when
            val exception = shouldThrow<AdminInsufficientPermissionsException> {
                organizationFacade.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
                )
            }

            // then
            exception.message shouldBe "User requested for admin role has insufficient permissions"
        }
    }

    forAll(
        row(User.Role.EPISTIMI_ADMIN),
        row(User.Role.ORGANIZATION_ADMIN),
    ) { role ->
        should("successfully register a new organization if provided admin has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(role, "admin_id")
            every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(User.Role.TEACHER, "director_id")
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null
            every { organizationRepository.save(any()) } returnsArgument 0
            every { locationClient.getLocation(any()) } returns null
            every { organizationDetailsDecorator.decorate(ofType(Organization::class)) } returns TestData.organizationDetails

            // expect
            shouldNotThrow<AdminInsufficientPermissionsException> {
                organizationFacade.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
                )
            }

            // and
            verify {
                organizationRepository.save(
                    Organization(
                        id = null,
                        name = "ABC",
                        adminId = UserId("admin_id"),
                        status = Organization.Status.ENABLED,
                        address = TestData.address,
                        location = null,
                    )
                )
            }
        }
    }

    should("fail to register a new organization if admin with provided id does not exist") {
        // given
        every { userRepository.findById(UserId("admin_id")) } throws UserNotFoundException()
        every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(User.Role.TEACHER, "director_id")
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

        // when
        val exception = shouldThrow<AdminNotFoundException> {
            organizationFacade.registerOrganization(
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
            )
        }

        // then
        exception.message shouldBe "Admin with id admin_id does not exist"
    }

    forAll(
        row(User.Role.STUDENT),
        row(User.Role.PARENT),
    ) { role ->
        should("fail to register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(User.Role.ORGANIZATION_ADMIN, "admin_id")
            every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(role, "director_id")
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

            // when
            val exception = shouldThrow<DirectorInsufficientPermissionsException> {
                organizationFacade.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
                )
            }

            // then
            exception.message shouldBe "User requested for director role has insufficient permissions"
        }
    }

    forAll(
        row(User.Role.EPISTIMI_ADMIN),
        row(User.Role.ORGANIZATION_ADMIN),
        row(User.Role.TEACHER),
    ) { role ->
        should("successfully register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(User.Role.ORGANIZATION_ADMIN, "admin_id")
            every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(role, "director_id")
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null
            every { locationClient.getLocation(any()) } returns null
            every { organizationDetailsDecorator.decorate(ofType(Organization::class)) } returns TestData.organizationDetails

            // expect
            shouldNotThrow<DirectorInsufficientPermissionsException> {
                organizationFacade.registerOrganization(
                    OrganizationRegisterRequest("ABC", UserId("admin_id"), TestData.address)
                )
            }

            // and
            verify {
                organizationRepository.save(
                    Organization(
                        id = null,
                        name = "ABC",
                        adminId = UserId("admin_id"),
                        status = Organization.Status.ENABLED,
                        address = TestData.address,
                        location = null,
                    )
                )
            }
        }
    }

    should("fail to register a new organization if provided director with provided id does not exist") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(User.Role.ORGANIZATION_ADMIN, "admin_id")
        every { userRepository.findById(UserId("director_id")) } throws UserNotFoundException()
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null

        // when
        val exception = shouldThrow<DirectorNotFoundException> {
            organizationFacade.registerOrganization(
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
            )
        }

        // then
        exception.message shouldBe "Director with id director_id does not exist"
    }

    should("fail to register a new organization if provided admin already manages other organization") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(User.Role.ORGANIZATION_ADMIN, "admin_id")
        every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(User.Role.TEACHER, "director_id")
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns
                Organization(
                    id = OrganizationId("123"),
                    name = "SP7",
                    adminId = UserId("admin_id"),
                    status = Organization.Status.ENABLED,
                    address = TestData.address,
                    location = null,
                )

        // when
        val exception = shouldThrow<AdminManagingOtherOrganizationException> {
            organizationFacade.registerOrganization(
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
            )
        }

        // then
        exception.message shouldBe "Provided admin is already managing other organization"
    }
})
