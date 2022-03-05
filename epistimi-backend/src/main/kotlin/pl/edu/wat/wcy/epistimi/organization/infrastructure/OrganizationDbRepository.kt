package pl.edu.wat.wcy.epistimi.organization.infrastructure

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.Organization
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
        id = this.id!!,
        name = this.name,
        admin = userDbRepository.findById(this.adminId),
        status = Organization.Status.valueOf(this.status)
    )

    override fun findById(organizationId: String): Organization =
        organizationMongoDbRepository.findById(organizationId)
            .map { it.toDomain() }
            .orElseThrow { throw OrganizationNotFoundException() }

    override fun insert(organization: Organization): Organization =
        organizationMongoDbRepository.insert(
            OrganizationMongoDbDocument(
                id = null,
                name = organization.name,
                adminId = organization.admin.id!!.value,
                status = organization.status.toString()
            )
        ).toDomain()

    override fun save(organization: Organization): Organization =
        organizationMongoDbRepository.save(
            OrganizationMongoDbDocument(
                id = organization.id,
                name = organization.name,
                adminId = organization.admin.id!!.value,
                status = organization.status.toString()
            )
        ).toDomain()
}
