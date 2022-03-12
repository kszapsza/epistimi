package pl.edu.wat.wcy.epistimi.user.dto

import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User

data class UserRegisterRequest(
    val firstName: String,
    val lastName: String,
    val role: User.Role,
    val username: String,
    val password: String,
    val pesel: String? = null,
    val sex: User.Sex? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val address: Address? = null,
)
