package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.shared.Address

data class OrganizationRegisterRequest(
    val name: String,
    val adminId: String,
    val directorId: String,
    val address: Address,
)

data class OrganizationChangeStatusRequest(
    val status: Organization.Status,
)
