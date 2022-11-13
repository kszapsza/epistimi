package pl.edu.wat.wcy.epistimi.user.domain.service

import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.port.UserRepository

class UserAggregatorService(
    private val userRepository: UserRepository,
) {
    fun getUsers(userRoles: List<UserRole>?): List<User> {
        return if (userRoles == null) {
            userRepository.findAll()
        } else {
            userRepository.findAllByRoleIn(userRoles)
        }
    }

    fun getUserById(userId: UserId): User {
        return userRepository.findById(userId)
    }
}
