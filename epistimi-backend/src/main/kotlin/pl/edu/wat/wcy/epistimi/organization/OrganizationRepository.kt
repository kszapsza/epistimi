package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.user.UserId

interface OrganizationRepository {
    fun exists(id: OrganizationId): Boolean
    fun findAll(): List<Organization>
    fun findAllByAdminId(adminId: UserId): List<Organization>
    fun findById(organizationId: OrganizationId): Organization
    fun save(organization: Organization): Organization
    fun update(organization: Organization): Organization
}
