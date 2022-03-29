package pl.edu.wat.wcy.epistimi.organization

interface OrganizationRepository {
    fun exists(id: OrganizationId): Boolean
    fun findAll(): List<Organization>
    fun findById(organizationId: OrganizationId): Organization
    fun save(organization: Organization): Organization
    fun update(organization: Organization): Organization
}
