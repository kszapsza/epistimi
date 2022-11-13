package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.data.DummyAddress
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationStatus
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationStatus.ENABLED
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.domain.User

@Component
internal class OrganizationStubbing(
    private val organizationRepository: OrganizationRepository,
) {
    fun organizationExists(
        id: OrganizationId? = null,
        name: String,
        admin: User,
        status: OrganizationStatus = ENABLED,
        address: Address = DummyAddress(),
        location: Location? = null,
    ): Organization {
        return organizationRepository.save(
            Organization(
                id = id,
                name = name,
                admin = admin,
                status = status,
                street = address.street,
                city = address.city,
                postalCode = address.postalCode,
                latitude = location?.latitude,
                longitude = location?.longitude,
            )
        )
    }
}
