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

    fun registerOrganization(registerRequest: OrganizationRegisterRequest): NewOrganization {
        return organizationRegistrar.registerOrganizationWithAdmin(registerRequest)
    }

    fun updateOrganization(
        organizationId: OrganizationId,
        updateRequest: OrganizationUpdateRequest,
    ): Organization {
        val updatedOrganization = organizationRepository.findById(organizationId)
            .copy(
                name = updateRequest.name,
                address = updateRequest.address,
                location = locationClient.getLocation(updateRequest.address),
            )
        return organizationRepository.update(updatedOrganization)
    }

    fun changeOrganizationStatus(
        organizationId: OrganizationId,
        changeStatusRequest: OrganizationChangeStatusRequest,
    ): Organization {
        return organizationRepository.save(
            organizationRepository.findById(organizationId).copy(status = changeStatusRequest.status)
        )
    }
}
