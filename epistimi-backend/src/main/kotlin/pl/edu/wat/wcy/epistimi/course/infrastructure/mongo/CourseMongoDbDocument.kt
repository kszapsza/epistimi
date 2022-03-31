package pl.edu.wat.wcy.epistimi.course.infrastructure.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "courses")
data class CourseMongoDbDocument(
    @Id val id: String? = null,
    @Indexed val organizationId: String,
    val code: Code,
    val schoolYear: String,
    val classTeacherId: String,
    val studentIds: List<String>,
) {
    data class Code(
        val number: Int,
        val letter: String,
    )
}
