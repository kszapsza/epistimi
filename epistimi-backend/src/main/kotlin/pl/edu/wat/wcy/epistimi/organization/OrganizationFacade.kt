package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.organization.OrganizationRegistrar.NewOrganization
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository

class OrganizationFacade(
    private val organizationRegistrar: OrganizationRegistrar,
    private val organizationRepository: OrganizationRepository,
    private val locationClient: OrganizationLocationClient,
    private val detailsDecorator: OrganizationDetailsDecorator,
) {
    fun getOrganization(organizationId: OrganizationId): OrganizationDetails {
        return organizationRepository.findById(organizationId)
            .let { detailsDecorator.decorate(it) }
    }

    fun getOrganizations(): List<OrganizationDetails> {
        return organizationRepository.findAll()
            .map { detailsDecorator.decorate(it) }
    }

    fun registerOrganization(registerRequest: OrganizationRegisterRequest): NewOrganization {
        return organizationRegistrar.registerOrganizationWithAdmin(registerRequest)
    }

    fun updateOrganization(
        organizationId: OrganizationId,
        updateRequest: OrganizationUpdateRequest,
    ): OrganizationDetails {
        val updatedOrganization = organizationRepository.findById(organizationId)
            .copy(
                name = updateRequest.name,
                address = updateRequest.address,
                location = locationClient.getLocation(updateRequest.address),
            )
        return organizationRepository.update(updatedOrganization)
            .let { detailsDecorator.decorate(it) }
    }

    fun changeOrganizationStatus(
        organizationId: OrganizationId,
        changeStatusRequest: OrganizationChangeStatusRequest,
    ): OrganizationDetails {
        return organizationRepository.save(
            organizationRepository.findById(organizationId).copy(status = changeStatusRequest.status)
        ).let {
            detailsDecorator.decorate(it)
        }
    }
}
