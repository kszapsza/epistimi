package pl.edu.wat.wcy.epistimi.student.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentJpaRepository : JpaRepository<StudentJpaEntity, UUID> {
    fun findFirstByUserId(userId: UUID): StudentJpaEntity?
}
