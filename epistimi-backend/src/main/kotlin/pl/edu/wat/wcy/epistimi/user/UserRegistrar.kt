package pl.edu.wat.wcy.epistimi.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

@Service
class UserRegistrar(
    private val userRepository: UserRepository,
    private val credentialsGenerator: UserCredentialsGenerator,
    private val passwordEncoder: PasswordEncoder,
) {
    data class NewUser(
        val user: User,
        val password: String,
    )

    fun registerUser(request: UserRegisterRequest): NewUser {
        val username = request.username ?: credentialsGenerator.generateUsername(request.firstName, request.lastName)
        val password = request.password ?: credentialsGenerator.generatePassword()

        return NewUser(
            user = registerUser(request, username, password),
            password = password,
        )
    }

    private fun registerUser(
        registerRequest: UserRegisterRequest,
        username: String,
        password: String,
    ): User {
        return userRepository.save(
            User(
                id = null,
                firstName = registerRequest.firstName,
                lastName = registerRequest.lastName,
                role = registerRequest.role,
                username = username,
                passwordHash = passwordEncoder.encode(password),
                pesel = registerRequest.pesel,
                sex = registerRequest.sex,
                email = registerRequest.email,
                phoneNumber = registerRequest.phoneNumber,
                address = registerRequest.address,
            )
        )
    }
}
