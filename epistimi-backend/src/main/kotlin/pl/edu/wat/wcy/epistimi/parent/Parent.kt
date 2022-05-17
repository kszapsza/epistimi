package pl.edu.wat.wcy.epistimi.parent

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

/*
 * TODO: User(role=PARENT) logs in and can select from header,
 *   which child school profile (determined with Student class) he would like to use
 */

data class Parent(
    val id: ParentId? = null,
    val userId: UserId,
    val organizationId: OrganizationId,
)

@JvmInline
value class ParentId(
    val value: String,
)

data class ParentDetails(
    val id: ParentId? = null,
    val user: User,
    val organization: Organization,
)
