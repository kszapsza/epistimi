package pl.edu.wat.wcy.epistimi.parent.adapter.rest

import pl.edu.wat.wcy.epistimi.parent.ParentDetails
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse
import pl.edu.wat.wcy.epistimi.user.adapter.rest.toUserResponse

data class ParentResponse(
    val id: ParentId? = null,
    val user: UserResponse,
)

fun ParentDetails.toParentResponse() = ParentResponse(
    id = id,
    user = user.toUserResponse(),
)
