package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser

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
        val organizationAdminUser = registerOrganizationAdminUser(registerRequest)
        val organization = registerOrganization(registerRequest, organizationAdminUser)

        return NewOrganization(
            organization = organization,
            admin = organizationAdminUser,
        )
    }

    private fun registerOrganizationAdminUser(registerRequest: OrganizationRegisterRequest): NewUser {
        return userRegistrar.registerUser(
            registerRequest.admin.copy(role = ORGANIZATION_ADMIN)
        )
    }

    private fun registerOrganization(
        registerRequest: OrganizationRegisterRequest,
        organizationAdminUser: NewUser
    ): Organization {
        return organizationRepository.save(
            Organization(
                id = null,
                name = registerRequest.name,
                adminId = organizationAdminUser.user.id!!,
                status = Organization.Status.ENABLED,
                address = registerRequest.address,
                location = retrieveLocation(registerRequest),
            )
        )
    }

    private fun retrieveLocation(registerRequest: OrganizationRegisterRequest): Location? {
        return locationClient.getLocation(registerRequest.address)
    }
}
