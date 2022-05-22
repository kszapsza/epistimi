package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserRegistrar

internal class OrganizationRegistrarTest : ShouldSpec({
    val organizationRepository = mockk<OrganizationRepository>()
    val userRegistrar = mockk<UserRegistrar>()
    val locationClient = mockk<OrganizationLocationClient>()

    val organizationRegistrar = OrganizationRegistrar(
        organizationRepository,
        userRegistrar,
        locationClient,
    )

    // TODO
})
