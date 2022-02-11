package pl.edu.wat.wcy.epistimi.user.dto

import pl.edu.wat.wcy.epistimi.user.User

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val type: User.Type,
) {
    companion object {
        fun fromDomain(user: User) = UserResponse(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            type = user.type
        )
    }
}
