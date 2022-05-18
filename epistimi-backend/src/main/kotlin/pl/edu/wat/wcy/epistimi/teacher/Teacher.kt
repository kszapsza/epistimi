package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

data class Teacher(
    val id: TeacherId? = null,
    val userId: UserId,
    val organizationId: OrganizationId,
    val academicTitle: String?,
)

@JvmInline
value class TeacherId(
    val value: String,
)

data class TeacherDetails(
    val id: TeacherId? = null,
    val user: User,
    val organization: Organization,
    val academicTitle: String?,
)
