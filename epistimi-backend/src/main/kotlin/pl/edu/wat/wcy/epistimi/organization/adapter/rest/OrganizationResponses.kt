package pl.edu.wat.wcy.epistimi.organization.adapter.rest

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

data class OrganizationResponse(
    val id: OrganizationId,
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
