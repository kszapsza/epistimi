package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.user.User
import java.util.UUID

data class Teacher(
    val id: TeacherId? = null,
    val user: User,
    val organization: Organization,
    val academicTitle: String?,
)

@JvmInline
value class TeacherId(
    val value: UUID,
)