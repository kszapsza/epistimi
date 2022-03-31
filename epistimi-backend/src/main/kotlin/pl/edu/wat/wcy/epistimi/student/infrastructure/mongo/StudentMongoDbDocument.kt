package pl.edu.wat.wcy.epistimi.student.infrastructure.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "students")
data class StudentMongoDbDocument(
    @Id val id: String? = null,
    @Indexed val userId: String,
    val organizationId: String,
    val parentIds: List<String>,
)
