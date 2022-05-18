package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.UserId

data class OrganizationRegisterRequest(
    val name: String,
    val adminId: UserId,
    val directorId: UserId,
    val address: Address,
)

data class OrganizationChangeStatusRequest(
    val status: Organization.Status,
)
