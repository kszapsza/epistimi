package pl.edu.wat.wcy.epistimi.teacher.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TeacherMongoDbRepository : MongoRepository<TeacherMongoDbDocument, String> {
    fun findAllByOrganizationId(organizationId: String): List<TeacherMongoDbDocument>
}
