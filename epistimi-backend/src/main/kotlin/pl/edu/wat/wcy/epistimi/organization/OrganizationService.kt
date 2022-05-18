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

    fun registerOrganization(registerRequest: OrganizationRegisterRequest): OrganizationDetails {
        validateAdmin(registerRequest.adminId)
        validateDirector(registerRequest.directorId)

        return organizationRepository.save(
            Organization(
                id = null,
                name = registerRequest.name,
                adminId = registerRequest.adminId,
                status = ENABLED,
                directorId = registerRequest.directorId,
                address = registerRequest.address,
                location = locationClient.getLocation(registerRequest.address),
            )
        ).let {
            detailsDecorator.decorate(it)
        }
    }

    private fun validateAdmin(adminId: UserId) {
        adminId
            .let { userId -> getUserOrThrow(userId) { AdminNotFoundException(userId) } }
            .also { user -> if (!user.isEligibleToBeAdmin()) throw AdminInsufficientPermissionsException() }
            .also { user -> if (user.managesOtherOrganization()) throw AdminManagingOtherOrganizationException() }
    }

    private fun validateDirector(directorId: UserId) {
        directorId
            .let { userId -> getUserOrThrow(userId) { DirectorNotFoundException(userId) } }
            .also { user -> if (!user.isEligibleToBeDirector()) throw DirectorInsufficientPermissionsException() }
    }

    private fun getUserOrThrow(
        userId: UserId,
        exceptionSupplier: () -> Exception,
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
    ): OrganizationDetails {
        validateAdminForUpdate(updateRequest.adminId, organizationId)
        validateDirector(updateRequest.directorId)

        return organizationRepository.update(
            Organization(
                id = organizationId,
                name = updateRequest.name,
                adminId = updateRequest.adminId,
                status = ENABLED,
                directorId = updateRequest.directorId,
                address = updateRequest.address,
                location = locationClient.getLocation(updateRequest.address),
            )
        ).let {
            detailsDecorator.decorate(it)
        }
    }

    private fun validateAdminForUpdate(adminId: UserId, organizationId: OrganizationId): User {
        return adminId
            .let { userId -> getUserOrThrow(userId) { AdminNotFoundException(userId) } }
            .also { user -> if (!user.isEligibleToBeAdmin()) throw AdminInsufficientPermissionsException() }
            .also { user -> if (user.managesOrganizationOtherThanUpdated(organizationId)) throw AdminManagingOtherOrganizationException() }
    }

    private fun User.managesOrganizationOtherThanUpdated(
        updatedOrganizationId: OrganizationId,
    ): Boolean {
        return organizationRepository.findFirstByAdminId(id!!)
            ?.let { organization -> organization.id != updatedOrganizationId }
            ?: false
    }

    fun changeOrganizationStatus(
        organizationId: OrganizationId,
        changeStatusRequest: OrganizationChangeStatusRequest,
    ): OrganizationDetails {
        return organizationRepository.save(
            organizationRepository.findById(organizationId)
                .copy(status = changeStatusRequest.status)
        ).let {
            detailsDecorator.decorate(it)
        }
    }

    companion object {
        private val ALLOWED_ADMIN_ROLES = arrayOf(ORGANIZATION_ADMIN, EPISTIMI_ADMIN)
        private val ALLOWED_DIRECTOR_ROLES = arrayOf(TEACHER, ORGANIZATION_ADMIN, EPISTIMI_ADMIN)
    }
}
