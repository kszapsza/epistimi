package pl.edu.wat.wcy.epistimi.course.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseMongoDbRepository : MongoRepository<CourseMongoDbDocument, String> {
    fun findAllByOrganizationId(organizationId: String): List<CourseMongoDbDocument>
}
