package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.ParentDetails
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

data class Student(
    val id: StudentId? = null,
    val userId: UserId,
    val organizationId: OrganizationId,
    val parentsIds: List<ParentId>,
)

@JvmInline
value class StudentId(
    val value: String,
)

data class StudentDetails(
    val id: StudentId? = null,
    val user: User,
    val organization: Organization,
    val parents: List<ParentDetails>,
)
