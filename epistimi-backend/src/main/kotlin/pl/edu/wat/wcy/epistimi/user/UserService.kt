package pl.edu.wat.wcy.epistimi.user

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun getUserById(userId: String): User =
        userRepository.findById(userId)

    fun registerUser(registerRequest: UserRegisterRequest) =
        /*
            TODO: (when there will be Spring Security configured)
                *   EPISTIMI_ADMIN can register anyone
                *   ORGANIZATION_ADMIN can register anyone apart from EPISTIMI_ADMIN
                *   other groups cannot register anyone at all
        */
        userRepository.insert(
            User(
                id = "",
                firstName = registerRequest.firstName,
                lastName = registerRequest.lastName,
                type = registerRequest.type
            )
        )

}