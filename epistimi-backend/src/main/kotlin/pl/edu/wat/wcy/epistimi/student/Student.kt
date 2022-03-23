package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.parent.ParentId

/*
 * TODO: User(role=STUDENT) logs in and can select from header,
 *   which school profile (determined with Student class) he would like to use
 */

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

