package pl.edu.wat.wcy.epistimi.user.infrastructure

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Repository
class UserDbRepository(
    private val userMongoDbRepository: UserMongoDbRepository
) : UserRepository {

    override fun findAll(): List<User> {
        return userMongoDbRepository.findAll()
            .map { it.toDomain() }
    }

    override fun findById(userId: String): User {
        return userMongoDbRepository.findById(userId)
            .map { it.toDomain() }
            .orElseThrow { throw UserNotFoundException() }
    }

    override fun insert(user: User): User {
        return userMongoDbRepository.save(
            UserMongoDbDocument(
                id = null,
                firstName = user.firstName,
                lastName = user.lastName,
                accountType = user.type.toString()
            )
        ).toDomain()
    }

    override fun save(user: User): User {
        return userMongoDbRepository.save(
            UserMongoDbDocument(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                accountType = user.type.toString()
            )
        ).toDomain()
    }

}
