package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import pl.edu.wat.wcy.epistimi.user.UserRole

class OrganizationRegistrar(
    private val organizationRepository: OrganizationRepository,
    private val userRegistrar: UserRegistrar,
    private val locationClient: OrganizationLocationClient,
) {
    data class NewOrganization(
        val organization: Organization,
        val admin: NewUser,
    )

    fun registerOrganizationWithAdmin(registerRequest: OrganizationRegisterRequest): NewOrganization {
        val organization = registerOrganization(registerRequest)
        val organizationAdminUser = registerOrganizationAdmin(registerRequest, organization)

        return NewOrganization(
            organization = organization,
            admin = organizationAdminUser,
        )
    }

    private fun registerOrganizationAdmin(
        registerRequest: OrganizationRegisterRequest,
        organization: Organization,
    ): NewUser {
        return userRegistrar.registerUser(
            contextOrganization = organization,
            request = registerRequest.admin.copy(role = UserRole.ORGANIZATION_ADMIN)
        )
    }

    private fun registerOrganization(registerRequest: OrganizationRegisterRequest): Organization {
        val location = retrieveLocation(registerRequest)
        return organizationRepository.save(
            Organization(
                id = null,
                name = registerRequest.name,
                status = OrganizationStatus.ENABLED,
                admins = emptySet(),
                street = registerRequest.address.street,
                postalCode = registerRequest.address.postalCode,
                city = registerRequest.address.city,
                latitude = location?.latitude,
                longitude = location?.longitude,
            )
        )
    }

    private fun retrieveLocation(registerRequest: OrganizationRegisterRequest): Location? {
        return locationClient.getLocation(registerRequest.address)
    }
}
