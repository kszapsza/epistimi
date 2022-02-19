package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

data class OrganizationResponse(
    val id: String,
    val name: String,
    val admin: UserResponse,
    val status: String
)

data class OrganizationsResponse(
    val organizations: List<OrganizationResponse>
)
