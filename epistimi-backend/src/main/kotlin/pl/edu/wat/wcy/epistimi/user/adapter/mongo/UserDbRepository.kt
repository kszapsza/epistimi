package pl.edu.wat.wcy.epistimi.user.adapter.mongo

import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UsernameAlreadyInUseException
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Repository
class UserDbRepository(
    private val userMongoDbRepository: UserMongoDbRepository,
) : UserRepository {

    override fun findAll(): List<User> {
        return DbHandlers.handleDbMultiGet(mapper = UserDbBiMapper) {
            userMongoDbRepository.findAll()
        }
    }

    override fun findAllByRoleIn(roles: Collection<User.Role>): List<User> {
        return DbHandlers.handleDbMultiGet(mapper = UserDbBiMapper) {
            userMongoDbRepository.findAllByRoleIn(roles.map { it.toString() })
        }
    }

    override fun findById(userId: UserId): User {
        return DbHandlers.handleDbGet(mapper = UserDbBiMapper) {
            userMongoDbRepository.findById(userId.value)
                .orElseThrow { UserNotFoundException() }
        }
    }

    override fun findByUsername(username: String): User {
        return DbHandlers.handleDbGet(mapper = UserDbBiMapper) {
            try {
                userMongoDbRepository.findFirstByUsername(username)
            } catch (e: EmptyResultDataAccessException) {
                throw UserNotFoundException()
            }
        }
    }

    override fun findByUsernameStartingWith(usernamePrefix: String): List<User> {
        return DbHandlers.handleDbMultiGet(mapper = UserDbBiMapper) {
            userMongoDbRepository.findByUsernameStartingWith(usernamePrefix)
        }
    }

    override fun save(user: User): User {
        return DbHandlers.handleDbInsert(mapper = UserDbBiMapper, domainObject = user) {
            try {
                userMongoDbRepository.save(it)
            } catch (e: DuplicateKeyException) {
                throw UsernameAlreadyInUseException(user.username)
            }
        }
    }

    override fun saveAll(users: List<User>): List<User> {
        return DbHandlers.handleDbMultiInsert(
            mapper = UserDbBiMapper,
            domainObjects = users,
            dbCall = userMongoDbRepository::saveAll
        )
    }

}
