package pl.edu.wat.wcy.epistimi.user.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import java.util.UUID

interface UserJpaRepository : JpaRepository<User, UUID> {
    fun findAllByRoleIn(role: Collection<UserRole>): List<User>
    fun findFirstByUsername(username: String): User?
    fun findAllByUsernameStartingWith(username: String): List<User>
}
