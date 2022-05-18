package pl.edu.wat.wcy.epistimi.parent.infrastructure.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "parents")
data class ParentMongoDbDocument(
    @Id val id: String? = null,
    @Indexed val userId: String,
    val organizationId: String,
)
