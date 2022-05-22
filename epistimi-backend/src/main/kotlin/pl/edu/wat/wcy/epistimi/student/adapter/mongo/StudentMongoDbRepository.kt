package pl.edu.wat.wcy.epistimi.student.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
interface StudentMongoDbRepository : MongoRepository<StudentMongoDbDocument, String> {
    fun findFirstByUserId(userId: UserId): StudentMongoDbDocument?
}
