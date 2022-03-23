package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

data class Course(
    val id: CourseId? = null,
    val organizationId: OrganizationId,
    val code: String,
    val schoolYear: String,
    val classTeacherId: TeacherId,
    val studentIds: List<StudentId>,
)

@JvmInline
value class CourseId(
    val value: String
)
