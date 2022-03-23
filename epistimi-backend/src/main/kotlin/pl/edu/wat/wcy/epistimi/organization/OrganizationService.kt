package pl.edu.wat.wcy.epistimi.organization

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
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
                admin = tryRetrieveAdmin(registerRequest.adminId),
                status = ENABLED,
                director = tryRetrieveDirector(registerRequest.directorId),
                address = registerRequest.address,
            )
        )
    }

    private fun tryRetrieveAdmin(adminId: String): User {
        return try {
            userRepository.findById(adminId)
                .also { validateOrganizationAdminRole(it) }
        } catch (e: UserNotFoundException) {
            throw AdministratorNotFoundException()
        }
    }

    private fun validateOrganizationAdminRole(admin: User) {
        if (admin.role !in ALLOWED_ADMIN_ROLES) {
            logger.warn("Attempted to register an organization with user ineligible to be an organization admin")
            throw AdministratorInsufficientPermissionsException()
        }
    }

    private fun tryRetrieveDirector(directorId: String): User {
        return try {
            userRepository.findById(directorId)
                .also { validateOrganizationDirectorRole(it) }
        } catch (e: UserNotFoundException) {
            throw DirectorNotFoundException()
        }
    }

    private fun validateOrganizationDirectorRole(director: User) {
        if (director.role !in ALLOWED_DIRECTOR_ROLES) {
            logger.warn("Attempted to register an organization with user ineligible to be an organization director")
            throw DirectorInsufficientPermissionsException()
        }
    }

    fun updateOrganization(
        organizationId: String,
        updateRequest: OrganizationRegisterRequest,
    ): Organization {
        return organizationRepository.update(
            Organization(
                id = OrganizationId(organizationId),
                name = updateRequest.name,
                admin = tryRetrieveAdmin(updateRequest.adminId),
                status = ENABLED,
                director = tryRetrieveDirector(updateRequest.directorId),
                address = updateRequest.address,
            )
        )
    }

    fun changeOrganizationStatus(
        organizationId: String,
        changeStatusRequest: OrganizationChangeStatusRequest,
    ): Organization {
        return organizationRepository.save(
            organizationRepository.findById(organizationId)
                .copy(status = changeStatusRequest.status)
        )
    }

    companion object {
        private val logger by logger()
        private val ALLOWED_ADMIN_ROLES = arrayOf(ORGANIZATION_ADMIN, EPISTIMI_ADMIN)
        private val ALLOWED_DIRECTOR_ROLES = arrayOf(TEACHER, ORGANIZATION_ADMIN, EPISTIMI_ADMIN)
    }
}
