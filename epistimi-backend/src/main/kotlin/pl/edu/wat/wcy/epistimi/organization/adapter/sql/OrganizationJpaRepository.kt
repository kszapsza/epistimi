package pl.edu.wat.wcy.epistimi.organization.adapter.sql;

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrganizationJpaRepository : JpaRepository<OrganizationJpaEntity, UUID> {
    fun findFirstByAdminId(adminId: UUID): OrganizationJpaEntity?
}