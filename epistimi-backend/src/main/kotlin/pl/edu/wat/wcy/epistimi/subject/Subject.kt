package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

data class Subject(
    val id: SubjectId? = null,
    val name: String,
    val courseId: CourseId,
    val teacherId: TeacherId,
)

@JvmInline
value class SubjectId(
    val value: String,
)
