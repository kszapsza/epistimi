package pl.edu.wat.wcy.epistimi.user.adapter.rest

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

data class UserRegisterResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: User.Role,
    val username: String,
    val password: String,
    val pesel: String?,
    val sex: User.Sex?,
    val email: String?,
    val phoneNumber: String?,
    val address: Address?,
) {
    constructor(user: User, password: String) : this(
        id = user.id!!.value,
        firstName = user.firstName,
        lastName = user.lastName,
        role = user.role,
        username = user.username,
        password = password,
        pesel = user.pesel,
        sex = user.sex,
        email = user.email,
        phoneNumber = user.phoneNumber,
        address = user.address,
    )
}
