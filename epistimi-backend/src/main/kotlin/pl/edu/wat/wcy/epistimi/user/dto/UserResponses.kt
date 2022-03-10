package pl.edu.wat.wcy.epistimi.user.dto

import pl.edu.wat.wcy.epistimi.user.User

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: User.Role,
    val username: String,
    val sex: User.Sex?,
)

data class UsersResponse(
    val users: List<UserResponse>,
)
