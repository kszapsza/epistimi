package pl.edu.wat.wcy.epistimi.parent.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.ParentRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class ParentDbRepository(
    private val parentMongoDbRepository: ParentMongoDbRepository,
) : ParentRepository {

    override fun findByIds(ids: List<ParentId>): List<Parent> {
        return parentMongoDbRepository.findAllById(ids.map { it.value })
            .map { it.toDomain() }
    }

    override fun save(parent: Parent): Parent {
        return parent.run {
            parentMongoDbRepository.save(
                ParentMongoDbDocument(
                    id = null,
                    userId = parent.userId.value,
                    organizationId = parent.organizationId.value,
                )
            )
        }.toDomain()
    }

    private fun ParentMongoDbDocument.toDomain() = Parent(
        id = ParentId(id!!),
        userId = UserId(userId),
        organizationId = OrganizationId(organizationId),
    )

    override fun saveAll(parents: List<Parent>): List<Parent> {
        return parents
            .map { parent ->
                ParentMongoDbDocument(
                    id = null,
                    userId = parent.userId.value,
                    organizationId = parent.organizationId.value,
                )
            }
            .let { parentDocuments -> parentMongoDbRepository.saveAll(parentDocuments) }
            .map { parentDocument -> parentDocument.toDomain() }
    }
}
