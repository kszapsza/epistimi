package pl.edu.wat.wcy.epistimi.user.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserMongoDbRepository : MongoRepository<UserMongoDbDocument, String> {
    fun findFirstByUsername(username: String): UserMongoDbDocument
}
