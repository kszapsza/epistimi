package pl.edu.wat.wcy.epistimi.parent.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ParentMongoDbRepository : MongoRepository<ParentMongoDbDocument, String>
