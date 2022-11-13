package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.domain.service.OrganizationRegistrationService
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService

internal class OrganizationRegistrarTest : ShouldSpec({
    val organizationRepository = mockk<OrganizationRepository>()
    val userRegistrationService = mockk<UserRegistrationService>()
    val locationClient = mockk<OrganizationLocationClient>()

    val organizationRegistrationService = OrganizationRegistrationService(
        organizationRepository,
        userRegistrationService,
        locationClient,
    )

    // TODO
})
