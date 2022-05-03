package pl.edu.wat.wcy.epistimi.user

import org.springframework.stereotype.Service

@Service
class UserAggregator(
    private val userRepository: UserRepository,
) {
    fun getUsers(userRoles: List<User.Role>?): List<User> {
        return if (userRoles == null) {
            userRepository.findAll()
        } else {
            userRepository.findAllByRoleIn(userRoles)
        }
    }

    fun getUserById(userId: UserId) = userRepository.findById(userId)
}
