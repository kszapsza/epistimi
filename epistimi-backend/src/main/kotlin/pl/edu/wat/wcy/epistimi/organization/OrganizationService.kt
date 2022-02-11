package pl.edu.wat.wcy.epistimi.organization

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationResponse
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationsResponse
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository
) {
    fun getOrganizations(): OrganizationsResponse {
        return OrganizationsResponse(
            organizationRepository.findAll()
                .map { it.toResponse() }
        )
    }

    private fun Organization.toResponse(): OrganizationResponse {
        val admin = userRepository.findById(this.adminId)
        return OrganizationResponse(
            id = this.id,
            name = this.name,
            admin = UserResponse.fromDomain(admin),
            status = this.status.toString()
        )
    }

    fun registerOrganization(registerRequest: OrganizationRegisterRequest): OrganizationResponse {
        val admin = try {
            userRepository.findById(registerRequest.adminId)
        } catch (e: UserNotFoundException) {
            throw AdministratorNotFoundException()
        }
        if (admin.type != User.Type.EPISTIMI_ADMIN
            && admin.type != User.Type.ORGANIZATION_EMPLOYEE) {
            throw AdministratorInsufficientPermissionsException()
        }
        return organizationRepository.insert(
            Organization(
                id = "",
                name = registerRequest.name,
                adminId = registerRequest.adminId,
                status = Organization.Status.ENABLED
            )
        ).toResponse()
    }

    fun changeOrganizationStatus(
        organizationId: String,
        changeStatusRequest: OrganizationChangeStatusRequest
    ): OrganizationResponse {
        val organization = organizationRepository.findById(organizationId)
        return organizationRepository.save(
            Organization(
                id = organization.id,
                name = organization.name,
                adminId = organization.adminId,
                status = changeStatusRequest.status
            )
        ).toResponse()
    }

}
