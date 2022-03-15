package pl.edu.wat.wcy.epistimi.parent

import pl.edu.wat.wcy.epistimi.user.UserId

/*
 * TODO: User(role=PARENT) logs in and can select from header,
 *   which child school profile (determined with Student class) he would like to use
 */

data class Parent(
    val id: ParentId? = null,
    val userId: UserId,
)

@JvmInline
value class ParentId(
    val value: String,
)
