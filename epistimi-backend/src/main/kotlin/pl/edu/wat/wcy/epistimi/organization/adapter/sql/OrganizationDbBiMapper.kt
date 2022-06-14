package pl.edu.wat.wcy.epistimi.organization.adapter.sql

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserDbBiMapper

object OrganizationDbBiMapper : BiMapper<Organization, OrganizationJpaEntity> {
    override fun toDomain(entity: OrganizationJpaEntity) = with(entity) {
        Organization(
            id = OrganizationId(id!!),
            name = name,
            admin = UserDbBiMapper.toDomain(admin),
            status = Organization.Status.valueOf(status),
            address = Address(
                street = street,
                postalCode = postalCode,
                city = city,
            ),
            location = if (latitude == null || longitude == null)
                null
            else Location(
                latitude = latitude,
                longitude = longitude,
            ),
        )
    }

    override fun fromDomain(domainObject: Organization) = with(domainObject) {
        OrganizationJpaEntity(
            id = id?.value,
            name = name,
            admin = UserDbBiMapper.fromDomain(admin),
            status = status.toString(),
            street = address.street,
            postalCode = address.postalCode,
            city = address.city,
            latitude = location?.latitude,
            longitude = location?.longitude,
        )
    }
}
