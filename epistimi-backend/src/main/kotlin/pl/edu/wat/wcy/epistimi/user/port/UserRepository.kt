package pl.edu.wat.wcy.epistimi.user.port

import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRole

interface UserRepository {
    fun findAll(): List<User>
    fun findAllByRoleIn(roles: List<UserRole>): List<User>
    fun findById(userId: UserId): User
    fun findByUsername(username: String): User
    fun findByUsernameStartingWith(usernamePrefix: String): List<User>
    fun save(user: User): User
    fun saveAll(users: List<User>): List<User>
}
