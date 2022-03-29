package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.teacher.Teacher

data class Course(
    val id: CourseId? = null,
    val organization: Organization,
    val code: Code,
    val schoolYear: String,
    val classTeacher: Teacher,
    val students: List<Student>,
) {
    data class Code(
        val number: Int,
        val letter: String,
    )
}

@JvmInline
value class CourseId(
    val value: String
)
