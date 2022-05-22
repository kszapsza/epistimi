package pl.edu.wat.wcy.epistimi.parent.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
interface ParentMongoDbRepository : MongoRepository<ParentMongoDbDocument, String> {
    fun findFirstByUserId(userId: UserId): ParentMongoDbDocument?
}
