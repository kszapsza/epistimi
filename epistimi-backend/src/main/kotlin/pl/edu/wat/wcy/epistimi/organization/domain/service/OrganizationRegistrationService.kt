package pl.edu.wat.wcy.epistimi.organization.domain.service

import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserFacade
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService.NewUser

class OrganizationRegistrationService(
    private val organizationRepository: OrganizationRepository,
    private val userFacade: UserFacade,
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
        return userFacade.registerUser(
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
