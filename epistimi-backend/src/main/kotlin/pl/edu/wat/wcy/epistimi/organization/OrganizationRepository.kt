package pl.edu.wat.wcy.epistimi.organization

interface OrganizationRepository {
    fun findAll(): List<Organization>
    fun findById(organizationId: String): Organization
    fun save(organization: Organization): Organization
    fun update(organization: Organization): Organization
}
