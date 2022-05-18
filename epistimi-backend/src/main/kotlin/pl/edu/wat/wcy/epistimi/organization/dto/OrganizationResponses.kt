package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.organization.OrganizationDetails
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

fun OrganizationDetails.toOrganizationResponse() = OrganizationResponse(
    id = id!!,
    name = name,
    admin = admin.toUserResponse(),
    status = status.toString(),
    director = director.toUserResponse(),
    address = address,
    location = location,
)

data class OrganizationsResponse(
    val organizations: List<OrganizationResponse>,
)
