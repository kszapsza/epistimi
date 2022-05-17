package pl.edu.wat.wcy.epistimi.parent.dto

import pl.edu.wat.wcy.epistimi.parent.ParentDetails
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse
import pl.edu.wat.wcy.epistimi.user.dto.toUserResponse

data class ParentResponse(
    val id: ParentId? = null,
    val user: UserResponse,
)

fun ParentDetails.toParentResponse() = ParentResponse(
    id = id,
    user = user.toUserResponse(),
)
