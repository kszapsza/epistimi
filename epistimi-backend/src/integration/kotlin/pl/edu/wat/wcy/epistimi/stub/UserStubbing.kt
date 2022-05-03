package pl.edu.wat.wcy.epistimi.stub

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.data.DummyAddress
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

@Component
internal class UserStubbing(
    private val userRegistrar: UserRegistrar,
    private val passwordEncoder: PasswordEncoder,
) {
    fun userExists(
        firstName: String = "Jan",
        lastName: String = "Kowalski",
        role: User.Role = STUDENT,
        username: String = "j.kowalski",
        password: String = "123456",
        pesel: String? = "10210155874",
        sex: User.Sex? = MALE,
        email: String? = "j.kowalski@gmail.com",
        phoneNumber: String? = "+48123456789",
        address: Address? = DummyAddress(),
    ): User {
        return userRegistrar.registerUser(
            UserRegisterRequest(firstName, lastName, role, username, password, pesel, sex, email, phoneNumber, address)
        ).user
    }

    val registerRequest
        get() = UserRegisterRequest(
            firstName = "Jan",
            lastName = "Kowalski",
            role = STUDENT,
            username = "j.kowalski",
            password = "123456",
            pesel = "10210155874",
            sex = MALE,
            email = "j.kowalski@gmail.com",
            phoneNumber = "+48123456789",
            address = DummyAddress(),
        )
}
