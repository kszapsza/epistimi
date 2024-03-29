package pl.edu.wat.wcy.epistimi.user.domain

import org.hibernate.validator.constraints.pl.PESEL
import pl.edu.wat.wcy.epistimi.common.Address
import javax.validation.Valid
import javax.validation.constraints.Email

data class UserRegisterRequest(
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val username: String? = null,
    val password: String? = null,

    @field:PESEL
    val pesel: String? = null,
    val sex: UserSex? = null,

    @field:Email
    val email: String? = null,

    val phoneNumber: String? = null,

    @field:Valid
    val address: Address? = null,
)
