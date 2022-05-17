package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.student.StudentDetails
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetails
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import java.time.LocalDate

data class Course(
    val id: CourseId? = null,
    val organizationId: OrganizationId,
    val code: Code,
    val schoolYear: String,
    val classTeacherId: TeacherId,
    val studentIds: List<StudentId>,
    val schoolYearBegin: LocalDate,
    val schoolYearSemesterEnd: LocalDate,
    val schoolYearEnd: LocalDate,
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
    val value: String,
)

data class CourseDetails(
    val id: CourseId? = null,
    val organization: Organization,
    val code: Course.Code,
    val schoolYear: String,
    val classTeacher: TeacherDetails,
    val students: List<StudentDetails>,
    val schoolYearBegin: LocalDate,
    val schoolYearSemesterEnd: LocalDate,
    val schoolYearEnd: LocalDate,
    val profile: String?,
    val profession: String?,
    val specialization: String?,
)
