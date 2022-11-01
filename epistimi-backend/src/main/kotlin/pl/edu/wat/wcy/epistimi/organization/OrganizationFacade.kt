package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.organization.OrganizationRegistrar.NewOrganization
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.User

class OrganizationFacade(
    private val organizationRegistrar: OrganizationRegistrar,
    private val organizationRepository: OrganizationRepository,
    private val locationClient: OrganizationLocationClient,
) {
    fun getOrganization(organizationId: OrganizationId): Organization {
        return organizationRepository.findById(organizationId)
    }

    fun getOrganizations(): List<Organization> {
        return organizationRepository.findAll()
    }

    fun registerOrganization(
        requesterUser: User,
        registerRequest: OrganizationRegisterRequest,
    ): NewOrganization {
        return organizationRegistrar.registerOrganizationWithAdmin(requesterUser, registerRequest)
    }

    fun updateOrganization(
        organizationId: OrganizationId,
        updateRequest: OrganizationUpdateRequest,
    ): Organization {
        val updatedOrganization = organizationRepository.findById(organizationId)
        val updatedLocation = locationClient.getLocation(updateRequest.address)

        return organizationRepository.update(
            Organization(
                id = updatedOrganization.id,
                name = updateRequest.name,
                status = updatedOrganization.status,
                admin = updatedOrganization.admin,
                street = updateRequest.address.street,
                postalCode = updateRequest.address.postalCode,
                city = updateRequest.address.city,
                latitude = updatedLocation?.latitude,
                longitude = updatedLocation?.longitude,
            )
        )
    }

    fun changeOrganizationStatus(
        organizationId: OrganizationId,
        changeStatusRequest: OrganizationChangeStatusRequest,
    ): Organization {
        val updatedOrganization = organizationRepository.findById(organizationId)
        return organizationRepository.save(
            Organization(
                id = updatedOrganization.id,
                name = updatedOrganization.name,
                status = changeStatusRequest.status,
                admin = updatedOrganization.admin,
                street = updatedOrganization.street,
                postalCode = updatedOrganization.postalCode,
                city = updatedOrganization.city,
                latitude = updatedOrganization.latitude,
                longitude = updatedOrganization.longitude,
            )
        )
    }
}
