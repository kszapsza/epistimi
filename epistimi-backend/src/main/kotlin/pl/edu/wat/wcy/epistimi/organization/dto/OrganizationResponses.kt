package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse
import pl.edu.wat.wcy.epistimi.user.dto.toUserResponse

data class OrganizationResponse(
    val id: OrganizationId,
    val name: String,
    val admin: UserResponse,
    val status: String,
    val director: UserResponse,
    val address: Address,
    val location: Location?,
)

fun Organization.toOrganizationResponse() = OrganizationResponse(
    id = this.id!!,
    name = this.name,
    admin = this.admin.toUserResponse(),
    status = this.status.toString(),
    director = this.director.toUserResponse(),
    address = this.address,
    location = this.location,
)

data class OrganizationsResponse(
    val organizations: List<OrganizationResponse>,
)
