package pl.edu.wat.wcy.epistimi.organization.infrastructure

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.infrastructure.UserDbRepository

@Repository
class OrganizationDbRepository(
    private val organizationMongoDbRepository: OrganizationMongoDbRepository,
    private val userDbRepository: UserDbRepository,
) : OrganizationRepository {

    override fun findAll(): List<Organization> =
        organizationMongoDbRepository.findAll()
            .map { it.toDomain() }

    private fun OrganizationMongoDbDocument.toDomain() = Organization(
        id = OrganizationId(this.id!!),
        name = this.name,
        admin = userDbRepository.findById(this.adminId),
        status = Organization.Status.valueOf(this.status),
        director = userDbRepository.findById(this.directorId),
        address = this.address,
    )

    override fun findById(organizationId: String): Organization =
        organizationMongoDbRepository.findById(organizationId)
            .map { it.toDomain() }
            .orElseThrow { throw OrganizationNotFoundException() }

    override fun save(organization: Organization): Organization =
        organization.toMongoDbDocument()
            .let { organizationMongoDbRepository.save(it) }
            .toDomain()

    private fun Organization.toMongoDbDocument() =
        OrganizationMongoDbDocument(
            id = this.id?.value,
            name = this.name,
            adminId = this.admin.id!!.value,
            status = this.status.toString(),
            directorId = this.director.id!!.value,
            address = this.address,
        )

    override fun update(organization: Organization): Organization {
        val existingOrganization = organizationMongoDbRepository
                .findById(organization.id!!.value)
                .orElseThrow { throw OrganizationNotFoundException() }
        return organization.toMongoDbDocument().let {
                organizationMongoDbRepository.save(
                    it.copy(status = existingOrganization.status)
                )
            }.toDomain()
    }
}
