package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.UserId

/*
 * TODO: User(role=TEACHER) logs in and can select from header,
 *   which school profile (determined with following Teacher class) he would like to use
 */

data class Teacher(
    val id: TeacherId? = null,
    val userId: UserId,
    val organizationId: OrganizationId,
    val academicTitle: String,
)

@JvmInline
value class TeacherId(
    val value: String
)
