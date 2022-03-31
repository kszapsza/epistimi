package pl.edu.wat.wcy.epistimi.organization

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.user.User
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
                admin = registerRequest.adminId
                    .let { userId -> getUserOrThrow(userId) { AdminNotFoundException(userId) } }
                    .also { user -> if (!user.isEligibleToBeAdmin()) throw AdminInsufficientPermissionsException() }
                    .also { user -> if (user.managesOtherOrganization()) throw AdminManagingOtherOrganizationException() },
                status = ENABLED,
                director = registerRequest.directorId
                    .let { userId -> getUserOrThrow(userId) { DirectorNotFoundException(userId) } }
                    .also { user -> if (!user.isEligibleToBeDirector()) throw DirectorInsufficientPermissionsException() },
                address = registerRequest.address,
                location = locationClient.getLocation(registerRequest.address),
            )
        )
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
        return organizationRepository.findAllByAdminId(id!!).isNotEmpty()
    }

    fun updateOrganization(
        organizationId: OrganizationId,
        updateRequest: OrganizationRegisterRequest,
    ): Organization {
        return organizationRepository.update(
            Organization(
                id = organizationId,
                name = updateRequest.name,
                admin = updateRequest.adminId
                    .let { userId -> getUserOrThrow(userId) { AdminNotFoundException(userId) } }
                    .also { user -> if (!user.isEligibleToBeAdmin()) throw AdminInsufficientPermissionsException() }
                    .also { user -> if (user.managesOrganizationOtherThanUpdated(organizationId)) throw AdminManagingOtherOrganizationException() },
                status = ENABLED,
                director = updateRequest.directorId
                    .let { userId -> getUserOrThrow(userId) { DirectorNotFoundException(userId) } }
                    .also { user -> if (!user.isEligibleToBeDirector()) throw DirectorInsufficientPermissionsException() },
                address = updateRequest.address,
                location = locationClient.getLocation(updateRequest.address),
            )
        )
    }

    private fun User.managesOrganizationOtherThanUpdated(
        updatedOrganizationId: OrganizationId
    ): Boolean {
        return organizationRepository.findAllByAdminId(id!!)
            .let { organizations ->
                when (organizations.size) {
                    0 -> false
                    1 -> organizations[0].id != updatedOrganizationId
                    else -> true
                }
            }
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
        private val ALLOWED_ADMIN_ROLES = arrayOf(User.Role.ORGANIZATION_ADMIN, User.Role.EPISTIMI_ADMIN)
        private val ALLOWED_DIRECTOR_ROLES = arrayOf(User.Role.TEACHER, User.Role.ORGANIZATION_ADMIN, User.Role.EPISTIMI_ADMIN)
    }
}
