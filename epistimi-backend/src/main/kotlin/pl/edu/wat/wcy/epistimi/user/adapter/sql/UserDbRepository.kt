package pl.edu.wat.wcy.epistimi.user.adapter.sql

import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import pl.edu.wat.wcy.epistimi.user.domain.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.UsernameAlreadyInUseException
import pl.edu.wat.wcy.epistimi.user.domain.port.UserRepository

@Repository
class UserDbRepository(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {

    override fun findAll(): List<User> {
        return userJpaRepository.findAll()
    }

    override fun findAllByRoleIn(roles: List<UserRole>): List<User> {
        return userJpaRepository.findAllByRoleIn(roles)
    }

    override fun findById(userId: UserId): User {
        return userJpaRepository.findById(userId.value)
            .orElseThrow { UserNotFoundException() }
    }

    override fun findByUsername(username: String): User {
        return userJpaRepository.findFirstByUsername(username) ?: throw UserNotFoundException()
    }

    override fun findByUsernameStartingWith(usernamePrefix: String): List<User> {
        return userJpaRepository.findAllByUsernameStartingWith(usernamePrefix)
    }

    override fun save(user: User): User {
        return try {
            userJpaRepository.save(user)
        } catch (e: DuplicateKeyException) {
            throw UsernameAlreadyInUseException(user.username)
        }
    }

    override fun saveAll(users: List<User>): List<User> {
        return userJpaRepository.saveAll(users)
    }
}
