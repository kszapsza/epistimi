package pl.edu.wat.wcy.epistimi.parent.adapter.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.ParentNotFoundException
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class ParentDbRepository(
    private val parentMongoDbRepository: ParentMongoDbRepository,
) : ParentRepository {

    override fun findByUserId(id: UserId): Parent {
        return DbHandlers.handleDbGet(mapper = ParentDbBiMapper) {
            parentMongoDbRepository.findFirstByUserId(id.value)
                ?: throw ParentNotFoundException()
        }
    }

    override fun findByIds(ids: List<ParentId>): List<Parent> {
        return DbHandlers.handleDbMultiGet(mapper = ParentDbBiMapper) {
            parentMongoDbRepository.findAllById(ids.map { it.value })
        }
    }

    override fun save(parent: Parent): Parent {
        return DbHandlers.handleDbInsert(
            mapper = ParentDbBiMapper,
            domainObject = parent,
            dbCall = parentMongoDbRepository::save
        )
    }

    override fun saveAll(parents: List<Parent>): List<Parent> {
        return DbHandlers.handleDbMultiInsert(
            mapper = ParentDbBiMapper,
            domainObjects = parents,
            dbCall = parentMongoDbRepository::saveAll
        )
    }
}
