package pl.edu.wat.wcy.epistimi.parent.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.ParentRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Repository
class ParentDbRepository(
    private val parentMongoDbRepository: ParentMongoDbRepository,
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository,
) : ParentRepository {

    override fun findByIds(ids: List<ParentId>): List<Parent> {
        return parentMongoDbRepository.findAllById(ids.map { it.value })
            .map { it.toDomain() }
    }

    private fun ParentMongoDbDocument.toDomain() = Parent(
        id = ParentId(id!!),
        user = userRepository.findById(UserId(userId)),
        organization = organizationRepository.findById(OrganizationId(organizationId)),
    )

    override fun save(parent: Parent): Parent {
        return parent.run {
            parentMongoDbRepository.save(
                ParentMongoDbDocument(
                    id = null,
                    userId = parent.user.id!!.value,
                    organizationId = parent.organization.id!!.value,
                )
            )
        }.toDomain()
    }
}
