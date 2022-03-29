package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.data.DummyAddress
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location
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
        director: User,
        address: Address = DummyAddress(),
        location: Location? = null,
    ): Organization {
        return organizationRepository.save(
            Organization(
                id = id,
                name = name,
                admin = admin,
                status = status,
                director = director,
                address = address,
                location = location,
            )
        )
    }
}
