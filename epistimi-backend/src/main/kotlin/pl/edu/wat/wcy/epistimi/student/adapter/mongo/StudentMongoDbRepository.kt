package pl.edu.wat.wcy.epistimi.student.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentMongoDbRepository : MongoRepository<StudentMongoDbDocument, String> {
    fun findFirstByUserId(userId: String): StudentMongoDbDocument?
}
