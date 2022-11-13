package pl.edu.wat.wcy.epistimi.parent.domain.port

import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.parent.domain.ParentId
import pl.edu.wat.wcy.epistimi.user.domain.UserId

interface ParentRepository {
    fun findByUserId(id: UserId): Parent
    fun findByIds(ids: List<ParentId>): List<Parent>
    fun save(parent: Parent): Parent
    fun saveAll(parents: List<Parent>): List<Parent>
}
