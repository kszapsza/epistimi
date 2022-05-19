package pl.edu.wat.wcy.epistimi.parent.port

import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId

interface ParentRepository {
    fun findByIds(ids: List<ParentId>): List<Parent>
    fun save(parent: Parent): Parent
    fun saveAll(parents: List<Parent>): List<Parent>
}
