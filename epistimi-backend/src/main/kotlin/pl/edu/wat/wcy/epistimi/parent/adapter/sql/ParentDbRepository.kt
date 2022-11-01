package pl.edu.wat.wcy.epistimi.parent.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.ParentNotFoundException
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class ParentDbRepository(
    private val parentJpaRepository: ParentJpaRepository,
) : ParentRepository {

    override fun findByUserId(id: UserId): Parent {
        return parentJpaRepository.findFirstByUserId(id.value)
            ?: throw ParentNotFoundException()
    }

    override fun findByIds(ids: List<ParentId>): List<Parent> {
        return parentJpaRepository.findAllById(ids.map { it.value })
    }

    override fun save(parent: Parent): Parent {
        return parentJpaRepository.save(parent)
    }

    override fun saveAll(parents: List<Parent>): List<Parent> {
        return parentJpaRepository.saveAll(parents)
    }
}
