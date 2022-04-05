package pl.edu.wat.wcy.epistimi.course.infrastructure.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO
import java.util.Date

@Document(collection = "courses")
data class CourseMongoDbDocument(
    @Id val id: String? = null,
    @Indexed val organizationId: String,
    val code: Code,
    val schoolYear: String,
    val classTeacherId: String,
    val studentIds: List<String>,
    @DateTimeFormat(iso = ISO.DATE_TIME) val schoolYearBegin: Date,
    @DateTimeFormat(iso = ISO.DATE_TIME) val schoolYearSemesterEnd: Date,
    @DateTimeFormat(iso = ISO.DATE_TIME) val schoolYearEnd: Date,
    val profile: String?,
    val profession: String?,
    val specialization: String?,
) {
    data class Code(
        val number: String,
        val letter: String,
    )
}
