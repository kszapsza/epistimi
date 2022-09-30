package pl.edu.wat.wcy.epistimi.teacher.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TeacherJpaRepository : JpaRepository<TeacherJpaEntity, UUID> {
    fun findFirstByUserId(userId: UUID): TeacherJpaEntity?
    fun findAllByOrganizationId(organizationId: UUID): Collection<TeacherJpaEntity>
}
