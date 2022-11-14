package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.organization.domain.service.OrganizationRegistrationService

internal class OrganizationFacadeTest : ShouldSpec({
    val organizationRegistrationService = mockk<OrganizationRegistrationService>()
    val organizationRepository = mockk<OrganizationRepository>()
    val locationClient = mockk<OrganizationLocationClient>()

    val organizationFacade = OrganizationFacade(
        organizationRegistrationService,
        organizationRepository,
        locationClient,
    )

    // TODO
})
