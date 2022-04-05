package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import java.util.Date

data class Course(
    val id: CourseId? = null,
    val organization: Organization,
    val code: Code,
    val schoolYear: String,
    val classTeacher: Teacher,
    val students: List<Student>,
    val schoolYearBegin: Date,
    val schoolYearSemesterEnd: Date,
    val schoolYearEnd: Date,
    val profile: String? = null,
    val profession: String? = null,
    val specialization: String? = null,
) {
    data class Code(
        val number: String,
        val letter: String,
    )
}

@JvmInline
value class CourseId(
    val value: String
)
