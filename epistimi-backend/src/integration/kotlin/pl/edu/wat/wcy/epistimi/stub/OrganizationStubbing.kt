package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.data.DummyAddress
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.User

@Component
internal class OrganizationStubbing(
    private val organizationRepository: OrganizationRepository,
) {
    fun organizationExists(
        id: OrganizationId? = null,
        name: String,
        admin: User,
        status: Organization.Status = ENABLED,
        address: Address = DummyAddress(),
        location: Location? = null,
    ): Organization {
        return organizationRepository.save(
            Organization(
                id = id,
                name = name,
                adminId = admin.id!!,
                status = status,
                address = address,
                location = location,
            )
        )
    }
}
