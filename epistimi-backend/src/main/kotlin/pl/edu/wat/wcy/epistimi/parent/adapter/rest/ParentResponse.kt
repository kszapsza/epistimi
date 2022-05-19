package pl.edu.wat.wcy.epistimi.parent.adapter.rest

import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

data class ParentResponse(
    val id: ParentId? = null,
    val user: UserResponse,
)
