package pl.edu.wat.wcy.epistimi.organization.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import java.util.UUID

interface OrganizationJpaRepository : JpaRepository<Organization, UUID> {
    fun findFirstByAdminsContaining(adminId: UUID): Organization?
}
