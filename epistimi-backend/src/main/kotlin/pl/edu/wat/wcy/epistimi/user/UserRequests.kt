package pl.edu.wat.wcy.epistimi.user.dto

import org.hibernate.validator.constraints.pl.PESEL
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User
import javax.validation.constraints.Email

data class UserRegisterRequest(
    val firstName: String,
    val lastName: String,
    val role: User.Role,
    val username: String? = null,
    val password: String? = null,

    @field:PESEL
    val pesel: String? = null,
    val sex: User.Sex? = null,

    @field:Email
    val email: String? = null,

    val phoneNumber: String? = null,
    val address: Address? = null,
)
