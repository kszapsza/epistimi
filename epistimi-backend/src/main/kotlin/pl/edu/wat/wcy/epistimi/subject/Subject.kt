package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import java.util.UUID

data class Subject(
    val id: SubjectId? = null,
    val name: String,
    val course: Course,
    val teacher: Teacher,
)

@JvmInline
value class SubjectId(
    val value: UUID,
)
