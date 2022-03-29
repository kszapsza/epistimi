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
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val locationClient: OrganizationLocationClient,
) {
    fun getOrganization(organizationId: OrganizationId): Organization {
        return organizationRepository.findById(organizationId)
    }

    fun getOrganizations(): List<Organization> {
        return organizationRepository.findAll()
    }

    fun registerOrganization(registerRequest: OrganizationRegisterRequest): Organization {
        val admin = tryRetrieveAdmin(registerRequest.adminId)
        val director = tryRetrieveDirector(registerRequest.directorId)
        val location = locationClient.getLocation(registerRequest.address)

        return organizationRepository.save(
            Organization(
                id = null,
                name = registerRequest.name,
                admin = admin,
                status = ENABLED,
                director = director,
                address = registerRequest.address,
                location = location,
            )
        )
    }

    private fun tryRetrieveAdmin(adminId: UserId): User {
        return try {
            userRepository.findById(adminId)
                .also { validateOrganizationAdminRole(it) }
        } catch (e: UserNotFoundException) {
            throw AdministratorNotFoundException(adminId)
        }
    }

    private fun validateOrganizationAdminRole(admin: User) {
        if (admin.role !in ALLOWED_ADMIN_ROLES) {
            logger.warn("Attempted to register an organization with user ineligible to be an organization admin")
            throw AdministratorInsufficientPermissionsException()
        }
    }

    private fun tryRetrieveDirector(directorId: UserId): User {
        return try {
            userRepository.findById(directorId)
                .also { validateOrganizationDirectorRole(it) }
        } catch (e: UserNotFoundException) {
            throw DirectorNotFoundException(directorId)
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
        val admin = tryRetrieveAdmin(updateRequest.adminId)
        val director = tryRetrieveDirector(updateRequest.directorId)
        val location = locationClient.getLocation(updateRequest.address)

        return organizationRepository.update(
            Organization(
                id = OrganizationId(organizationId),
                name = updateRequest.name,
                admin = admin,
                status = ENABLED,
                director = director,
                address = updateRequest.address,
                location = location,
            )
        )
    }

    fun changeOrganizationStatus(
        organizationId: OrganizationId,
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
