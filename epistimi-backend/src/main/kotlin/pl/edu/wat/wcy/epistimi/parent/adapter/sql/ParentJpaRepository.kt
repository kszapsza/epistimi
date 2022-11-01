package pl.edu.wat.wcy.epistimi.parent.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.parent.Parent
import java.util.UUID

interface ParentJpaRepository : JpaRepository<Parent, UUID> {
    fun findFirstByUserId(userId: UUID): Parent?
}
