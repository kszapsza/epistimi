package pl.edu.wat.wcy.epistimi.parent.infrastructure.mongo

import org.springframework.data.annotation.Id

data class ParentMongoDbDocument(
    @Id val id: String? = null,
    val userId: String,
    val organizationId: String,
)
