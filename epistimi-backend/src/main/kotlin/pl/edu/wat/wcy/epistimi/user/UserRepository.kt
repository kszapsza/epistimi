package pl.edu.wat.wcy.epistimi.user

interface UserRepository {
    fun findAll(): List<User>
    fun findAllByRoleIn(roles: Collection<User.Role>): List<User>
    fun findById(userId: UserId): User
    fun findByUsername(username: String): User
    fun findByUsernameStartingWith(usernamePrefix: String): List<User>
    fun save(user: User): User
    fun saveAll(users: List<User>): List<User>
}
