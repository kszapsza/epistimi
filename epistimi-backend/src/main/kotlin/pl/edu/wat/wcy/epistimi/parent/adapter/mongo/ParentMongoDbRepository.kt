package pl.edu.wat.wcy.epistimi.parent.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ParentMongoDbRepository : MongoRepository<ParentMongoDbDocument, String>
