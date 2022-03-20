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

    override fun findAllByRoleIn(roles: Collection<User.Role>): List<User> {
        return userMongoDbRepository.findAllByRoleIn(roles.map { it.toString() })
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

    override fun save(user: User): User {
        return try {
            userMongoDbRepository.save(
                UserMongoDbDocument(
                    id = user.id?.value,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    role = user.role.toString(),
                    username = user.username,
                    passwordHash = user.passwordHash,
                    pesel = user.pesel,
                    sex = user.sex?.toString(),
                    email = user.email,
                    phoneNumber = user.phoneNumber,
                    address = user.address,
                )
            ).toDomain()
        } catch (e: DuplicateKeyException) {
            throw UsernameAlreadyInUseException(user.username) // TODO: should be tested
        }
    }

}
