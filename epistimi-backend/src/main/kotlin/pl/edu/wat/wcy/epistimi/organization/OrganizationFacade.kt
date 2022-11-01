package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.organization.OrganizationRegistrar.NewOrganization
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository

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
        registerRequest: OrganizationRegisterRequest,
    ): NewOrganization {
        return organizationRegistrar.registerOrganizationWithAdmin(registerRequest)
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
                admins = updatedOrganization.admins,
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
                admins = updatedOrganization.admins,
                street = updatedOrganization.street,
                postalCode = updatedOrganization.postalCode,
                city = updatedOrganization.city,
                latitude = updatedOrganization.latitude,
                longitude = updatedOrganization.longitude,
            )
        )
    }
}
