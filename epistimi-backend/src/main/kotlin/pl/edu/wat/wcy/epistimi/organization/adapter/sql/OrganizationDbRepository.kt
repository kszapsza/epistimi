package pl.edu.wat.wcy.epistimi.organization.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
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
        return DbHandlers.handleDbMultiGet(
            mapper = OrganizationDbBiMapper,
            dbCall = organizationJpaRepository::findAll,
        )
    }

    override fun findFirstByAdminId(adminId: UserId): Organization? {
        return DbHandlers.handleDbGet(mapper = OrganizationDbBiMapper) {
            organizationJpaRepository.findFirstByAdminId(adminId.value) ?: return null
        }
    }

    override fun findById(organizationId: OrganizationId): Organization {
        return DbHandlers.handleDbGet(mapper = OrganizationDbBiMapper) {
            organizationJpaRepository.findById(organizationId.value)
                .orElseThrow { OrganizationNotFoundException(organizationId) }
        }
    }

    override fun save(organization: Organization): Organization {
        return DbHandlers.handleDbInsert(
            mapper = OrganizationDbBiMapper,
            domainObject = organization,
            dbCall = organizationJpaRepository::save,
        )
    }

    override fun update(organization: Organization): Organization {
        val existingOrganization = organizationJpaRepository
            .findById(organization.id!!.value)
            .orElseThrow { OrganizationNotFoundException(organization.id) }
        return DbHandlers.handleDbInsert(
            mapper = OrganizationDbBiMapper,
            domainObject = organization.copy(status = Organization.Status.valueOf(existingOrganization.status)),
        ) {
            organizationJpaRepository.save(it)
        }
    }
}
