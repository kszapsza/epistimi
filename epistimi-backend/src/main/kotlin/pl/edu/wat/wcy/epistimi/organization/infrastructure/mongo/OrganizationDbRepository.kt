package pl.edu.wat.wcy.epistimi.organization.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.infrastructure.mongo.UserDbRepository

@Repository
class OrganizationDbRepository(
    private val organizationMongoDbRepository: OrganizationMongoDbRepository,
    private val userDbRepository: UserDbRepository,
) : OrganizationRepository {

    override fun exists(id: OrganizationId): Boolean {
        return organizationMongoDbRepository.existsById(id.value)
    }

    override fun findAll(): List<Organization> {
        return organizationMongoDbRepository.findAll()
            .map { it.toDomain() }
    }

    override fun findAllByAdminId(adminId: UserId): List<Organization> {
        return organizationMongoDbRepository.findAllByAdminId(adminId.value)
            .map { it.toDomain() }
    }

    private fun OrganizationMongoDbDocument.toDomain() = Organization(
        id = OrganizationId(id!!),
        name = name,
        admin = userDbRepository.findById(UserId(adminId)),
        status = Organization.Status.valueOf(status),
        director = userDbRepository.findById(UserId(directorId)),
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
        id = this.id?.value,
        name = this.name,
        adminId = this.admin.id!!.value,
        status = this.status.toString(),
        directorId = this.director.id!!.value,
        address = this.address,
        location = this.location,
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
