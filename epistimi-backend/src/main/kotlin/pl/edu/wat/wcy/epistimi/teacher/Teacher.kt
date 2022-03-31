package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.user.User

/*
 * TODO: User(role=TEACHER) logs in and can select from header,
 *   which school profile (determined with following Teacher class) he would like to use
 */

data class Teacher(
    val id: TeacherId? = null,
    val user: User,
    val organization: Organization,
    val academicTitle: String?,
)

@JvmInline
value class TeacherId(
    val value: String
)
