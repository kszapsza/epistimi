package pl.edu.wat.wcy.epistimi.teacher.infrastructure.mongo

import org.springframework.data.annotation.Id

data class TeacherMongoDbDocument(
    @Id val id: String? = null,
    val userId: String,
    val organizationId: String,
    val academicTitle: String,
)
