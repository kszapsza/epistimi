package pl.edu.wat.wcy.epistimi.organization.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class OrganizationDbRepository(
    private val organizationMongoDbRepository: OrganizationMongoDbRepository,
) : OrganizationRepository {

    override fun exists(id: OrganizationId): Boolean {
        return organizationMongoDbRepository.existsById(id.value)
    }

    override fun findAll(): List<Organization> {
        return organizationMongoDbRepository.findAll()
            .map { it.toDomain() }
    }

    override fun findFirstByAdminId(adminId: UserId): Organization? {
        return organizationMongoDbRepository.findFirstByAdminId(adminId.value)?.toDomain()
    }

    private fun OrganizationMongoDbDocument.toDomain() = Organization(
        id = OrganizationId(id!!),
        name = name,
        adminId = UserId(adminId),
        status = Organization.Status.valueOf(status),
        directorId = UserId(directorId),
        address = address,
        location = location,
    )

    override fun findById(organizationId: OrganizationId): Organization =
        organizationMongoDbRepository.findById(organizationId.value)
            .map { it.toDomain() }
            .orElseThrow { OrganizationNotFoundException(organizationId) }

    override fun save(organization: Organization): Organization {
        return organization.toMongoDbDocument()
            .let { organizationMongoDbRepository.save(it) }
            .toDomain()
    }

    private fun Organization.toMongoDbDocument() = OrganizationMongoDbDocument(
        id = id?.value,
        name = name,
        adminId = adminId.value,
        status = status.toString(),
        directorId = directorId.value,
        address = address,
        location = location,
    )

    override fun update(organization: Organization): Organization {
        val existingOrganization = organizationMongoDbRepository
            .findById(organization.id!!.value)
            .orElseThrow { OrganizationNotFoundException(organization.id) }
        return organization.toMongoDbDocument().let {
            organizationMongoDbRepository.save(
                it.copy(status = existingOrganization.status)
            )
        }.toDomain()
    }
}
