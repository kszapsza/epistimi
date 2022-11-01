package pl.edu.wat.wcy.epistimi.organization.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class OrganizationDbRepository(
    private val organizationJpaRepository: OrganizationJpaRepository,
) : OrganizationRepository {

    override fun exists(id: OrganizationId): Boolean {
        return organizationJpaRepository.existsById(id.value)
    }

    override fun findAll(): List<Organization> {
        return organizationJpaRepository.findAll()
    }

    override fun findFirstByAdminId(adminId: UserId): Organization? {
        return organizationJpaRepository.findFirstByAdminId(adminId.value)

    }

    override fun findById(organizationId: OrganizationId): Organization {
        return organizationJpaRepository.findById(organizationId.value)
            .orElseThrow { OrganizationNotFoundException(organizationId) }
    }

    override fun save(organization: Organization): Organization {
        return organizationJpaRepository.save(organization)
    }

    override fun update(organization: Organization): Organization {
        organizationJpaRepository
            .findById(organization.id!!.value)
            .orElseThrow { OrganizationNotFoundException(organization.id) }
        return organizationJpaRepository.save(organization)
    }
}
