package pl.edu.wat.wcy.epistimi.organization

import org.springframework.stereotype.Service
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

    fun registerOrganization(registerRequest: OrganizationRegisterRequest): Organization {
        val admin = try {
            userRepository.findById(registerRequest.adminId)
        } catch (e: UserNotFoundException) {
            throw AdministratorNotFoundException()
        }
        if (admin.role != User.Role.EPISTIMI_ADMIN
            && admin.role != User.Role.ORGANIZATION_ADMIN) {
            throw AdministratorInsufficientPermissionsException()
        }
        return organizationRepository.insert(
            Organization(
                id = "",
                name = registerRequest.name,
                admin = admin,
                status = Organization.Status.ENABLED
            )
        )
    }

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
}
