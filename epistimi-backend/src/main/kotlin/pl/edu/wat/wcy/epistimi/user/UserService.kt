package pl.edu.wat.wcy.epistimi.user

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    fun getUsers(userRole: User.Role?): List<User> {
        return if (userRole == null) {
            userRepository.findAll()
        } else {
            userRepository.findAllByRole(userRole)
        }
    }

    fun getUserByUsername(username: String): User =
        userRepository.findByUsername(username)

    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN', 'ORGANIZATION_ADMIN')")
    fun getUserById(userId: String): User =
        userRepository.findById(userId)

    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN', 'ORGANIZATION_ADMIN')")
    fun registerUser(registerRequest: UserRegisterRequest): User =
        userRepository.insert(
            User(
                id = null,
                firstName = registerRequest.firstName,
                lastName = registerRequest.lastName,
                role = registerRequest.role,
                username = registerRequest.username,
                passwordHash = passwordEncoder.encode(registerRequest.password),
                sex = registerRequest.sex,
            )
        )
}
