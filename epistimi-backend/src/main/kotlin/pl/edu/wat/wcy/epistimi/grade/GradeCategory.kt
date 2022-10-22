package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.course.Course
import java.util.UUID

data class GradeCategory(
    val id: GradeCategoryId,
    val course: Course,
    val name: String,
    val color: String,
)

@JvmInline
value class GradeCategoryId(
    val value: UUID,
)
