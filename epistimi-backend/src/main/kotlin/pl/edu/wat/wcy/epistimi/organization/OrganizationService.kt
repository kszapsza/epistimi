package pl.edu.wat.wcy.epistimi.organization

import org.springframework.stereotype.Service
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
        return organizationRepository.save(
            Organization(
                id = null,
                name = registerRequest.name,
                admin = getAdmin(registerRequest.adminId),
                status = ENABLED,
                director = getDirector(registerRequest.directorId),
                address = registerRequest.address,
                location = locationClient.getLocation(registerRequest.address),
            )
        )
    }

    private fun getAdmin(adminId: UserId): User {
        return adminId
            .let { userId -> getUserOrThrow(userId) { AdminNotFoundException(userId) } }
            .also { user -> if (!user.isEligibleToBeAdmin()) throw AdminInsufficientPermissionsException() }
            .also { user -> if (user.managesOtherOrganization()) throw AdminManagingOtherOrganizationException() }
    }

    private fun getDirector(directorId: UserId): User {
        return directorId
            .let { userId -> getUserOrThrow(userId) { DirectorNotFoundException(userId) } }
            .also { user -> if (!user.isEligibleToBeDirector()) throw DirectorInsufficientPermissionsException() }
    }

    private fun getUserOrThrow(
        userId: UserId,
        exceptionSupplier: () -> Exception
    ): User {
        return try {
            userRepository.findById(userId)
        } catch (e: UserNotFoundException) {
            throw exceptionSupplier()
        }
    }

    private fun User.isEligibleToBeAdmin() = role in ALLOWED_ADMIN_ROLES
    private fun User.isEligibleToBeDirector() = role in ALLOWED_DIRECTOR_ROLES

    private fun User.managesOtherOrganization(): Boolean {
        return organizationRepository.findFirstByAdminId(id!!) != null
    }

    fun updateOrganization(
        organizationId: OrganizationId,
        updateRequest: OrganizationRegisterRequest,
    ): Organization {
        return organizationRepository.update(
            Organization(
                id = organizationId,
                name = updateRequest.name,
                admin = getAdminForUpdate(updateRequest.adminId, organizationId),
                status = ENABLED,
                director = getDirector(updateRequest.directorId),
                address = updateRequest.address,
                location = locationClient.getLocation(updateRequest.address),
            )
        )
    }

    private fun getAdminForUpdate(adminId: UserId, organizationId: OrganizationId): User {
        return adminId
            .let { userId -> getUserOrThrow(userId) { AdminNotFoundException(userId) } }
            .also { user -> if (!user.isEligibleToBeAdmin()) throw AdminInsufficientPermissionsException() }
            .also { user -> if (user.managesOrganizationOtherThanUpdated(organizationId)) throw AdminManagingOtherOrganizationException() }
    }

    private fun User.managesOrganizationOtherThanUpdated(
        updatedOrganizationId: OrganizationId
    ): Boolean {
        return organizationRepository.findFirstByAdminId(id!!)
            ?.let { organization -> organization.id != updatedOrganizationId }
            ?: false
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
        private val ALLOWED_ADMIN_ROLES = arrayOf(ORGANIZATION_ADMIN, EPISTIMI_ADMIN)
        private val ALLOWED_DIRECTOR_ROLES = arrayOf(TEACHER, ORGANIZATION_ADMIN, EPISTIMI_ADMIN)
    }
}
