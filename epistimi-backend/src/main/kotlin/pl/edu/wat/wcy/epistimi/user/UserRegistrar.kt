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
        val credentials = getCredentials(request)
        val user = userRepository.save(request.toUser(credentials))

        return NewUser(user = user, password = credentials.password)
    }

    private fun getCredentials(request: UserRegisterRequest): Credentials {
        return if (request.username == null || request.password == null) {
            credentialsGenerator.generate(request.firstName, request.lastName)
        } else {
            Credentials(
                username = request.username,
                password = request.password,
            )
        }
    }

    private fun UserRegisterRequest.toUser(credentials: Credentials): User {
        return User(
            id = null,
            firstName = firstName,
            lastName = lastName,
            role = role,
            username = credentials.username,
            passwordHash = passwordEncoder.encode(credentials.password),
            pesel = pesel,
            sex = sex,
            email = email,
            phoneNumber = phoneNumber,
            address = address,
        )
    }

    fun registerUsers(requests: List<UserRegisterRequest>): List<NewUser> {
        val credentials = requests.map { credentialsGenerator.generate(it.firstName, it.lastName) }
        val usersToRegister = requests.zip(credentials).map { (request, credentials) -> request.toUser(credentials) }

        return userRepository.saveAll(usersToRegister)
            .mapIndexed { index, user ->
                NewUser(
                    user = user,
                    password = credentials[index].password,
                )
            }
    }
}
