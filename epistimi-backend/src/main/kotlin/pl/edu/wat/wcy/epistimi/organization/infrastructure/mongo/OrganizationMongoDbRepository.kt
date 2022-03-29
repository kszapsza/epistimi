package pl.edu.wat.wcy.epistimi.organization.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationMongoDbRepository : MongoRepository<OrganizationMongoDbDocument, String>
