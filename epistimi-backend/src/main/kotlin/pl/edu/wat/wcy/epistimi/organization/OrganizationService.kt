package pl.edu.wat.wcy.epistimi.organization

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository
) {
    fun getOrganizations(): List<Organization> {
        return organizationRepository.findAll()
    }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    fun registerOrganization(registerRequest: OrganizationRegisterRequest): Organization {
        val requestedOrganizationAdmin = try {
            userRepository.findById(registerRequest.adminId)
        } catch (e: UserNotFoundException) {
            throw AdministratorNotFoundException()
        }
        if (!requestedOrganizationAdmin.isEligibleToBeOrganizationAdmin()) {
            logger.warn("Attempted to register an organization with user ineligible to be an organization admin")
            throw AdministratorInsufficientPermissionsException()
        }
        return organizationRepository.insert(
            Organization(
                id = null,
                name = registerRequest.name,
                admin = requestedOrganizationAdmin,
                status = Organization.Status.ENABLED
            )
        )
    }

    private fun User.isEligibleToBeOrganizationAdmin() =
        (role == User.Role.EPISTIMI_ADMIN || role == User.Role.ORGANIZATION_ADMIN)

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    fun changeOrganizationStatus(
        organizationId: String,
        changeStatusRequest: OrganizationChangeStatusRequest
    ): Organization {
        val organization = organizationRepository.findById(organizationId)
        return organizationRepository.save(
            Organization(
                id = organization.id,
                name = organization.name,
                admin = organization.admin,
                status = changeStatusRequest.status
            )
        )
    }
    
    companion object {
        private val logger by logger()
    }
}
