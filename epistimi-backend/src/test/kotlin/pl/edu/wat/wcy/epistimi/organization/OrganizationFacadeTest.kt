package pl.edu.wat.wcy.epistimi.organization

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.UserId

internal class OrganizationFacadeTest : ShouldSpec({
    val organizationRegistrar = mockk<OrganizationRegistrar>()
    val organizationRepository = mockk<OrganizationRepository>()
    val locationClient = mockk<OrganizationLocationClient>()
    val organizationDetailsDecorator = mockk<OrganizationDetailsDecorator>()

    val organizationFacade = OrganizationFacade(
        organizationRegistrar,
        organizationRepository,
        locationClient,
        organizationDetailsDecorator,
    )

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
            address = TestData.address,
            location = null,
        )
        every { locationClient.getLocation(any()) } returns null
        every { organizationDetailsDecorator.decorate(ofType(Organization::class)) } returns TestData.organizationDetails

        // expect
        shouldNotThrow<AdminManagingOtherOrganizationException> {
            organizationFacade.updateOrganization(
                OrganizationId("123"),
                OrganizationUpdateRequest("ABC", TestData.address)
            )
        }
    }
})
