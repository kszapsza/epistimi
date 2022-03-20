package pl.edu.wat.wcy.epistimi.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun getUsers(userRoles: List<User.Role>?): List<User> {
        return if (userRoles == null) {
            userRepository.findAll()
        } else {
            userRepository.findAllByRoleIn(userRoles)
        }
    }

    fun getUserByUsername(username: String): User =
        userRepository.findByUsername(username)

    fun getUserById(userId: String): User =
        userRepository.findById(userId)

    fun registerUser(registerRequest: UserRegisterRequest): User =
        userRepository.save(
            User(
                id = null,
                firstName = registerRequest.firstName,
                lastName = registerRequest.lastName,
                role = registerRequest.role,
                username = registerRequest.username,
                passwordHash = passwordEncoder.encode(registerRequest.password),
                pesel = registerRequest.pesel,
                sex = registerRequest.sex,
                email = registerRequest.email,
                phoneNumber = registerRequest.phoneNumber,
                address = registerRequest.address,
            )
        )
}
