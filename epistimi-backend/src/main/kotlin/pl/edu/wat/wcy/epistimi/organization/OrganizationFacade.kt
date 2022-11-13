package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.service.OrganizationRegistrationService.NewOrganization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.domain.service.OrganizationRegistrationService
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationUpdateRequest
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository

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
