package pl.edu.wat.wcy.epistimi.user.dto

import pl.edu.wat.wcy.epistimi.user.User

data class UserRegisterRequest(
    val firstName: String,
    val lastName: String,
    val type: User.Type,
)
