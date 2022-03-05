package pl.edu.wat.wcy.epistimi.parent

import pl.edu.wat.wcy.epistimi.user.UserId

data class Parent(
    val id: ParentId? = null,
    val userId: UserId,
)

@JvmInline
value class ParentId(
    val value: String,
)
