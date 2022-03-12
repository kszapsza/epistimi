package pl.edu.wat.wcy.epistimi.user.dto

import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: User.Role,
    val username: String,
    val pesel: String?,
    val sex: User.Sex?,
    val email: String?,
    val phoneNumber: String?,
    val address: Address?,
)

data class UsersResponse(
    val users: List<UserResponse>,
)
