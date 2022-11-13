package pl.edu.wat.wcy.epistimi.noticeboard.adapter.sql

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPost
import java.util.UUID

interface NoticeboardPostJpaRepository : JpaRepository<NoticeboardPost, UUID> {
    fun findAllByOrganizationId(organizationId: UUID, sort: Sort): List<NoticeboardPost>
}
