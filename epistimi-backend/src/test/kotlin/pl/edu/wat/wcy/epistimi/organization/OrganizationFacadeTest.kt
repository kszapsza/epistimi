package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository

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

    // TODO
})
