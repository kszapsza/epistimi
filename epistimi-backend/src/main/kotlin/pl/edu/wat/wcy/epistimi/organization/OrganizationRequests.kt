package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import javax.validation.constraints.NotBlank

data class OrganizationRegisterRequest(
    @field:NotBlank val name: String,
    val admin: UserRegisterRequest,
    val address: Address,
)

data class OrganizationChangeStatusRequest(
    val status: Organization.Status,
)

data class OrganizationUpdateRequest(
    @field:NotBlank val name: String,
    val address: Address,
)
