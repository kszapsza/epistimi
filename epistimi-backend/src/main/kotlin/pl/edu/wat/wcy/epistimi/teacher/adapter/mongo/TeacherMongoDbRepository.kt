package pl.edu.wat.wcy.epistimi.teacher.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TeacherMongoDbRepository : MongoRepository<TeacherMongoDbDocument, String> {
    fun findFirstByUserId(userId: String): TeacherMongoDbDocument?
    fun findAllByOrganizationId(organizationId: String): List<TeacherMongoDbDocument>
}
