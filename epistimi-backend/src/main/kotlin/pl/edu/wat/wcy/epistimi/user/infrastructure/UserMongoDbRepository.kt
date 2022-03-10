package pl.edu.wat.wcy.epistimi.user.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserMongoDbRepository : MongoRepository<UserMongoDbDocument, String> {
    fun findAllByRole(role: String): List<UserMongoDbDocument>
    fun findFirstByUsername(username: String): UserMongoDbDocument
}
