package pl.edu.wat.wcy.epistimi.user

import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

data class User(
    val id: UserId? = null,
    val firstName: String,
    val lastName: String,
    val role: Role,
    val username: String,
    val passwordHash: String,
    val pesel: String? = null,
    val sex: Sex? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val address: Address? = null, // TODO: new fields in mongo repository!
) {
    fun toResponse() = UserResponse(
        id = this.id!!.value,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        username = this.username,
        sex = this.sex,
    )

    enum class Role {
        EPISTIMI_ADMIN,
        ORGANIZATION_ADMIN,
        TEACHER,
        STUDENT,
        PARENT
    }

    enum class Sex {
        MALE, FEMALE, OTHER
    }
}

@JvmInline
value class UserId(
    val value: String
)
