package pl.edu.wat.wcy.epistimi.user.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserMongoDbRepository : MongoRepository<UserMongoDbDocument, String> {
    fun findAllByRoleIn(role: Collection<String>): List<UserMongoDbDocument>
    fun findFirstByUsername(username: String): UserMongoDbDocument
}
