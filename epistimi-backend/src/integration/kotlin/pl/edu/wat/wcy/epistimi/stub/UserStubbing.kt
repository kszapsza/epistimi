package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.data.DummyAddress
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRole
import pl.edu.wat.wcy.epistimi.user.UserRole.STUDENT
import pl.edu.wat.wcy.epistimi.user.UserSex
import pl.edu.wat.wcy.epistimi.user.UserSex.MALE

@Component
internal class UserStubbing(
    private val userRegistrar: UserRegistrar,
) {
    fun userExists(
        organization: Organization,
        firstName: String = "Jan",
        lastName: String = "Kowalski",
        role: UserRole = STUDENT,
        username: String = "j.kowalski",
        password: String = "123456",
        pesel: String? = "10210155874",
        sex: UserSex? = MALE,
        email: String? = "j.kowalski@gmail.com",
        phoneNumber: String? = "+48123456789",
        address: Address? = DummyAddress(),
    ): User {
        return userRegistrar.registerUser(
            organization,
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
