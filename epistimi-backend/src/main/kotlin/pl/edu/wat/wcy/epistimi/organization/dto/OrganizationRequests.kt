package pl.edu.wat.wcy.epistimi.organization.dto

import pl.edu.wat.wcy.epistimi.organization.Organization

data class OrganizationRegisterRequest(
    val name: String,
    val adminId: String
)

data class OrganizationChangeStatusRequest(
    val status: Organization.Status
)
