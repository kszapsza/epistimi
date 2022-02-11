package pl.edu.wat.wcy.epistimi.stub

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository

internal class InMemoryOrganizationRepository(
    private val organizations: MutableList<Organization> = mutableListOf()
) : OrganizationRepository {

    override fun findAll(): List<Organization> {
        return organizations.toList()
    }

    override fun findById(organizationId: String): Organization {
        return organizations.find { it.id == organizationId } ?: throw OrganizationNotFoundException()
    }

    override fun insert(organization: Organization): Organization {
        return organization
            .copy(id = organizations.size.toString())
            .also { organizations.add(it) }
    }

    override fun save(organization: Organization): Organization {
        if (organizations.find { it.id == organization.id } != null) {
            organizations.removeIf { it.id == organization.id }
        }
        return this.insert(organization)
    }
}
