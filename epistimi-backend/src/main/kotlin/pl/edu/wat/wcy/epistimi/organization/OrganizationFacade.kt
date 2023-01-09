package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationUpdateRequest
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.organization.domain.service.OrganizationRegistrationService
import pl.edu.wat.wcy.epistimi.organization.domain.service.OrganizationRegistrationService.NewOrganization

class OrganizationFacade(
    private val organizationRegistrationService: OrganizationRegistrationService,
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
        return organizationRegistrationService.registerOrganizationWithAdmin(registerRequest)
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
                admins = updatedOrganization.admins,
                street = updateRequest.address.street,
                postalCode = updateRequest.address.postalCode,
                city = updateRequest.address.city,
                latitude = updatedLocation?.latitude,
                longitude = updatedLocation?.longitude,
            )
        )
    }
}
