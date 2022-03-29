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

fun User.toUserResponse() = UserResponse(
    id = this.id!!.value,
    firstName = this.firstName,
    lastName = this.lastName,
    role = this.role,
    username = this.username,
    pesel = this.pesel,
    sex = this.sex,
    email = this.email,
    phoneNumber = this.phoneNumber,
    address = this.address,
)

data class UsersResponse(
    val users: List<UserResponse>,
)
