package pl.edu.wat.wcy.epistimi.user.infrastructure

import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository
import pl.edu.wat.wcy.epistimi.user.UsernameAlreadyInUseException

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

    override fun findByUsername(username: String): User {
        return try {
            userMongoDbRepository.findFirstByUsername(username).toDomain()
        } catch (e: EmptyResultDataAccessException) {
            throw UserNotFoundException()
        }
    }

    override fun insert(user: User): User {
        return try {
            userMongoDbRepository.insert(
                UserMongoDbDocument(
                    id = null,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    role = user.role.toString(),
                    username = user.username,
                    passwordHash = user.passwordHash,
                )
            ).toDomain()
        } catch (e: DuplicateKeyException) {
            throw UsernameAlreadyInUseException(user.username)
        }
    }

    override fun save(user: User): User {
        return userMongoDbRepository.save(
            UserMongoDbDocument(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                role = user.role.toString(),
                username = user.username,
                passwordHash = user.passwordHash,
            )
        ).toDomain()
    }

}
