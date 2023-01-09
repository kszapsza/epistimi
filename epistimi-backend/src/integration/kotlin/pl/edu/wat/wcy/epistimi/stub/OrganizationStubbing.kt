package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.fake.fakeAddress

@Component
internal class OrganizationStubbing(
    private val organizationRepository: OrganizationRepository,
) {
    fun organizationExists(
        id: OrganizationId? = null,
        name: String,
        admin: User,
        address: Address = fakeAddress,
        location: Location? = null,
    ): Organization {
        return organizationRepository.save(
            Organization(
                id = id,
                name = name,
                admins = setOf(admin),
                street = address.street,
                city = address.city,
                postalCode = address.postalCode,
                latitude = location?.latitude,
                longitude = location?.longitude,
            )
        )
    }
}
