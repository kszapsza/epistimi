package pl.edu.wat.wcy.epistimi.student.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentMongoDbRepository : MongoRepository<StudentMongoDbDocument, String>
