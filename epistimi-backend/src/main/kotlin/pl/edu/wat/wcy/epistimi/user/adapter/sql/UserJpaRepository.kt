package pl.edu.wat.wcy.epistimi.user.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserJpaEntity, UUID> {
    fun findAllByRoleIn(role: Collection<String>): Collection<UserJpaEntity>
    fun findFirstByUsername(username: String): UserJpaEntity?
    fun findAllByUsernameStartingWith(username: String): Collection<UserJpaEntity>
}
