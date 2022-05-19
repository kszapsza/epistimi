package pl.edu.wat.wcy.epistimi.organization.adapter.mongo

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.user.UserId

object OrganizationDbBiMapper : BiMapper<Organization, OrganizationMongoDbDocument> {
    override fun toDomain(entity: OrganizationMongoDbDocument) = with(entity) {
        Organization(
            id = OrganizationId(id!!),
            name = name,
            adminId = UserId(adminId),
            status = Organization.Status.valueOf(status),
            directorId = UserId(directorId),
            address = address,
            location = location,
        )
    }

    override fun fromDomain(domainObject: Organization) = with(domainObject) {
        OrganizationMongoDbDocument(
            id = id?.value,
            name = name,
            adminId = adminId.value,
            status = status.toString(),
            directorId = directorId.value,
            address = address,
            location = location,
        )
    }
}
