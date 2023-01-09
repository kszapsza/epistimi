package pl.edu.wat.wcy.epistimi.user.domain.port

import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

interface UserRepository {
    fun findAll(): List<User>
    fun findAllByRoleIn(roles: List<UserRole>): List<User>
    fun findById(userId: UserId): User
    fun findByUsername(username: String): User
    fun findByUsernameStartingWith(usernamePrefix: String): List<User>
    fun save(user: User): User
    fun saveAll(users: List<User>): List<User>
}
