package pl.edu.wat.wcy.epistimi.parent.adapter.sql;

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ParentJpaRepository : JpaRepository<ParentJpaEntity, UUID> {
    fun findFirstByUserId(userId: UUID): ParentJpaEntity?
}