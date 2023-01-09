package pl.edu.wat.wcy.epistimi.user.adapter.rest

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.UserSex
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val username: String,
    val pesel: String?,
    val sex: UserSex?,
    val email: String?,
    val phoneNumber: String?,
    val address: Address?,
)

data class UsersResponse(
    val users: List<UserResponse>,
)

data class UserRegisterResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val username: String,
    val password: String,
    val pesel: String?,
    val sex: UserSex?,
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
        address = Address.of(user.street, user.postalCode, user.city),
    )
}
