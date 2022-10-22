package pl.edu.wat.wcy.epistimi.noticeboard.adapter.sql

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NoticeboardPostJpaRepository : JpaRepository<NoticeboardPostJpaEntity, UUID> {
    fun findAllByOrganizationId(organizationId: UUID, sort: Sort): List<NoticeboardPostJpaEntity>
}