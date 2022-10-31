package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.subject.Subject
import java.util.UUID

data class GradeCategory(
    val id: GradeCategoryId,
    val subject: Subject,
    val name: String,
    val defaultWeight: Int,
    val color: String? = null,
)

@JvmInline
value class GradeCategoryId(
    val value: UUID,
)
