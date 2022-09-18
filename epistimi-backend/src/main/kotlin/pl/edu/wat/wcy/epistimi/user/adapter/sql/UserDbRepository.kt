package pl.edu.wat.wcy.epistimi.user.adapter.sql

import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UsernameAlreadyInUseException
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Repository
class UserDbRepository(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {

    override fun findAll(): List<User> {
        return DbHandlers.handleDbMultiGet(mapper = UserDbBiMapper) {
            userJpaRepository.findAll()
        }
    }

    override fun findAllByRoleIn(roles: Collection<User.Role>): List<User> {
        return DbHandlers.handleDbMultiGet(mapper = UserDbBiMapper) {
            userJpaRepository.findAllByRoleIn(roles.map { it.toString() })
        }
    }

    override fun findById(userId: UserId): User {
        return DbHandlers.handleDbGet(mapper = UserDbBiMapper) {
            userJpaRepository.findById(userId.value)
                .orElseThrow { UserNotFoundException() }
        }
    }

    override fun findByUsername(username: String): User {
        return DbHandlers.handleDbGet(mapper = UserDbBiMapper) {
                userJpaRepository.findFirstByUsername(username) ?: throw UserNotFoundException()
        }
    }

    override fun findByUsernameStartingWith(usernamePrefix: String): List<User> {
        return DbHandlers.handleDbMultiGet(mapper = UserDbBiMapper) {
            userJpaRepository.findAllByUsernameStartingWith(usernamePrefix)
        }
    }

    override fun save(user: User): User {
        return DbHandlers.handleDbInsert(mapper = UserDbBiMapper, domainObject = user) {
            try {
                userJpaRepository.save(it)
            } catch (e: DuplicateKeyException) {
                throw UsernameAlreadyInUseException(user.username)
            }
        }
    }

    override fun saveAll(users: List<User>): List<User> {
        return DbHandlers.handleDbMultiInsert(
            mapper = UserDbBiMapper,
            domainObjects = users,
            dbCall = userJpaRepository::saveAll
        )
    }
}
