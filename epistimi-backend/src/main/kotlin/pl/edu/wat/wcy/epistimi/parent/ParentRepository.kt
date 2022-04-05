package pl.edu.wat.wcy.epistimi.parent

interface ParentRepository {
    fun findByIds(ids: List<ParentId>): List<Parent>
    fun save(parent: Parent): Parent
}
