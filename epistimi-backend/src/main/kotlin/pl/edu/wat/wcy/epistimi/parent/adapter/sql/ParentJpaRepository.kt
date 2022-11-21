package pl.edu.wat.wcy.epistimi.parent.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import java.util.UUID

interface ParentJpaRepository : JpaRepository<Parent, UUID> {
    fun findByUserId(userId: UUID): Parent?
}
