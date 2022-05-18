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
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

internal class OrganizationFacadeTest : ShouldSpec({
    val organizationRepository = mockk<OrganizationRepository>()
    val userRepository = mockk<UserRepository>()
    val locationClient = mockk<OrganizationLocationClient>()
    val organizationDetailsDecorator = mockk<OrganizationDetailsDecorator>()

    val organizationFacade = OrganizationFacade(
        organizationRepository,
        userRepository,
        locationClient,
        organizationDetailsDecorator,
    )

    forAll(
        row(STUDENT),
        row(PARENT),
        row(TEACHER),
    ) { role ->
        should("fail to register a new organization if provided admin has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(role, "admin_id")
            every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(TEACHER, "director_id")
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
        row(EPISTIMI_ADMIN),
        row(ORGANIZATION_ADMIN),
    ) { role ->
        should("successfully register a new organization if provided admin has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(role, "admin_id")
            every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(TEACHER, "director_id")
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
                        status = ENABLED,
                        directorId = UserId("director_id"),
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
        every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(TEACHER, "director_id")
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
        row(STUDENT),
        row(PARENT),
    ) { role ->
        should("fail to register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(ORGANIZATION_ADMIN, "admin_id")
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
        row(EPISTIMI_ADMIN),
        row(ORGANIZATION_ADMIN),
        row(TEACHER),
    ) { role ->
        should("successfully register a new organization if provided director has $role role") {
            // given
            every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(ORGANIZATION_ADMIN, "admin_id")
            every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(role, "director_id")
            every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null
            every { locationClient.getLocation(any()) } returns null
            every { organizationDetailsDecorator.decorate(ofType(Organization::class)) } returns TestData.organizationDetails

            // expect
            shouldNotThrow<DirectorInsufficientPermissionsException> {
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
                        status = ENABLED,
                        directorId = UserId("director_id"),
                        address = TestData.address,
                        location = null,
                    )
                )
            }
        }
    }

    should("fail to register a new organization if provided director with provided id does not exist") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(ORGANIZATION_ADMIN, "admin_id")
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
        every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(ORGANIZATION_ADMIN, "admin_id")
        every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(TEACHER, "director_id")
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns
                Organization(
                    id = OrganizationId("123"),
                    name = "SP7",
                    adminId = UserId("admin_id"),
                    status = ENABLED,
                    directorId = UserId("director_id"),
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

    should("successfully update existing organization if admin was not changed") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(ORGANIZATION_ADMIN, "admin_id")
        every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(TEACHER, "director_id")
        every { organizationRepository.update(any()) } returnsArgument 0
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns Organization(
            id = OrganizationId("123"),
            name = "SP7",
            adminId = UserId("admin_id"),
            status = ENABLED,
            directorId = UserId("director_id"),
            address = TestData.address,
            location = null,
        )
        every { locationClient.getLocation(any()) } returns null
        every { organizationDetailsDecorator.decorate(ofType(Organization::class)) } returns TestData.organizationDetails

        // expect
        shouldNotThrow<AdminManagingOtherOrganizationException> {
            organizationFacade.updateOrganization(
                OrganizationId("123"),
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
            )
        }
    }

    should("successfully update existing organization if provided admin does not manage any organization yet") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(ORGANIZATION_ADMIN, "admin_id")
        every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(TEACHER, "director_id")
        every { organizationRepository.update(any()) } returnsArgument 0
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns null
        every { locationClient.getLocation(any()) } returns null

        // expect
        shouldNotThrow<AdminManagingOtherOrganizationException> {
            organizationFacade.updateOrganization(
                OrganizationId("123"),
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
            )
        }
    }

    should("fail updating existing organization, if provided new admin already manages other organization") {
        // given
        every { userRepository.findById(UserId("admin_id")) } returns TestData.Users.withRole(ORGANIZATION_ADMIN, "admin_id")
        every { userRepository.findById(UserId("director_id")) } returns TestData.Users.withRole(TEACHER, "director_id")
        every { organizationRepository.update(any()) } returnsArgument 0
        every { organizationRepository.findFirstByAdminId(UserId("admin_id")) } returns Organization(
            id = OrganizationId("some_different_id"),
            name = "G2",
            adminId = UserId("admin_id"),
            status = ENABLED,
            directorId = UserId("director_id"),
            address = TestData.address,
            location = null,
        )
        every { locationClient.getLocation(any()) } returns null

        // when
        val exception = shouldThrow<AdminManagingOtherOrganizationException> {
            organizationFacade.updateOrganization(
                OrganizationId("some_id"),
                OrganizationRegisterRequest("ABC", UserId("admin_id"), UserId("director_id"), TestData.address)
            )
        }

        // then
        exception.message shouldBe "Provided admin is already managing other organization"
    }
})
