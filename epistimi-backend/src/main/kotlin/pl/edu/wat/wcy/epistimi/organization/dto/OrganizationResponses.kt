package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

data class OrganizationResponse(
    val id: String,
    val name: String,
    val admin: UserResponse,
    val status: String
) {
    companion object {
        fun fromDomain(organization: Organization) = OrganizationResponse(
            id = organization.id,
            name = organization.name,
            admin = UserResponse.fromDomain(organization.admin),
            status = organization.status.toString()
        )
    }
}

data class OrganizationsResponse(
    val organizations: List<OrganizationResponse>
)
