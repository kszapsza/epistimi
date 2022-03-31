package pl.edu.wat.wcy.epistimi.teacher.infrastructure.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "teachers")
data class TeacherMongoDbDocument(
    @Id val id: String? = null,
    @Indexed val userId: String,
    val organizationId: String,
    val academicTitle: String?,
)
