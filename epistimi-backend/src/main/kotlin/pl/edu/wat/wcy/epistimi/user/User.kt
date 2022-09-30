package pl.edu.wat.wcy.epistimi.user

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.api.toUuid
import java.util.UUID

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
    val address: Address? = null,
) {
    enum class Role {
        EPISTIMI_ADMIN,
        ORGANIZATION_ADMIN,
        TEACHER,
        STUDENT,
        PARENT,
    }

    enum class Sex {
        MALE,
        FEMALE,
        OTHER,
    }
}

@JvmInline
value class UserId(
    val value: UUID,
) {
    constructor(value: String) : this(value = value.toUuid())
}
