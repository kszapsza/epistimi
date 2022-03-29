package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

data class OrganizationResponse(
    val id: String,
    val name: String,
    val admin: UserResponse,
    val status: String,
    val director: UserResponse,
    val address: Address,
    val location: Location?,
)

data class OrganizationsResponse(
    val organizations: List<OrganizationResponse>,
)
