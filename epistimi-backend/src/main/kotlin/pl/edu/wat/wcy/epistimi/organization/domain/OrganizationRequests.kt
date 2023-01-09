package pl.edu.wat.wcy.epistimi.organization.domain

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import javax.validation.constraints.NotBlank

data class OrganizationRegisterRequest(
    @field:NotBlank val name: String,
    val admin: UserRegisterRequest,
    val address: Address,
)

data class OrganizationUpdateRequest(
    @field:NotBlank val name: String,
    val address: Address,
)
