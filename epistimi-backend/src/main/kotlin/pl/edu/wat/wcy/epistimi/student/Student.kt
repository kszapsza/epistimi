package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.parent.ParentId

data class Student(
    val id: StudentId? = null,
    val userId: UserId,
    val organizationId: OrganizationId,
    val parentIds: List<ParentId>,
)

@JvmInline
value class StudentId(
    val value: String
)

