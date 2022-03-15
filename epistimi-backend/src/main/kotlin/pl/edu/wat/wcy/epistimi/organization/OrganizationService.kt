package pl.edu.wat.wcy.epistimi.organization

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
) {
    fun getOrganization(organizationId: String): Organization {
        return organizationRepository.findById(organizationId)
    }

    fun getOrganizations(): List<Organization> {
        return organizationRepository.findAll()
    }

    fun registerOrganization(registerRequest: OrganizationRegisterRequest): Organization {
        return organizationRepository.save(
            Organization(
                id = null,
                name = registerRequest.name,
                admin = tryRetrieveAdmin(registerRequest),
                status = ENABLED
            )
        )
    }

    private fun tryRetrieveAdmin(registerRequest: OrganizationRegisterRequest): User {
        return try {
            userRepository.findById(registerRequest.adminId)
                .also { validateOrganizationAdminRole(it) }
        } catch (e: UserNotFoundException) {
            throw AdministratorNotFoundException()
        }
    }

    private fun validateOrganizationAdminRole(user: User) {
        if (user.role !in ALLOWED_ADMIN_ROLES) {
            logger.warn("Attempted to register an organization with user ineligible to be an organization admin")
            throw AdministratorInsufficientPermissionsException()
        }
    }

    fun changeOrganizationStatus(
        organizationId: String,
        changeStatusRequest: OrganizationChangeStatusRequest
    ): Organization {
        return organizationRepository.save(
            organizationRepository.findById(organizationId)
                .copy(status = changeStatusRequest.status)
        )
    }

    companion object {
        private val logger by logger()
        private val ALLOWED_ADMIN_ROLES = arrayOf(ORGANIZATION_ADMIN, EPISTIMI_ADMIN)
    }
}
