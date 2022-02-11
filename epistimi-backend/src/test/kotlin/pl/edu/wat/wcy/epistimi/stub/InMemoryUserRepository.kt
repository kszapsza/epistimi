package pl.edu.wat.wcy.epistimi.stub

import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

internal class InMemoryUserRepository(
    private val users: MutableList<User> = mutableListOf()
) : UserRepository {

    override fun findAll(): List<User> {
        return users.toList()
    }

    override fun findById(userId: String): User {
        return users.find { it.id == userId } ?: throw UserNotFoundException()
    }

    override fun insert(user: User): User {
        return user.copy(id = users.size.toString())
            .also { users.add(it) }
    }

    override fun save(user: User): User {
        if (users.find { it.id == user.id } != null) {
            users.removeIf { it.id == user.id }
        }
        return this.insert(user)
    }
}
