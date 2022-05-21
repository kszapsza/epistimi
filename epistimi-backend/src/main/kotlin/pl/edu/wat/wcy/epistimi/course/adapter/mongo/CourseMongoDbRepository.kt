package pl.edu.wat.wcy.epistimi.course.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseMongoDbRepository
    : MongoRepository<CourseMongoDbDocument, String>, CourseQueryMongoDbRepository {
}
