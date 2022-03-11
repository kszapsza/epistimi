package pl.edu.wat.wcy.epistimi.user

interface UserRepository {
    fun findAll(): List<User>
    fun findAllByRole(role: User.Role): List<User>
    fun findById(userId: String): User
    fun findByUsername(username: String): User
    fun save(user: User): User
}
