package pl.edu.wat.wcy.epistimi.user

import pl.edu.wat.wcy.epistimi.user.port.UserRepository

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
