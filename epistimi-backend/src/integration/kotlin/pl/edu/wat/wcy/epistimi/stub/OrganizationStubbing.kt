package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.User

@Component
class OrganizationStubbing(
    private val organizationRepository: OrganizationRepository,
) {
    fun organizationExists(
        id: OrganizationId? = null,
        name: String,
        admin: User,
        status: Organization.Status = ENABLED
    ): Organization {
        return organizationRepository.save(
            Organization(
                id = id,
                name = name,
                admin = admin,
                status = status
            )
        )
    }
}
