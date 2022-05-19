package pl.edu.wat.wcy.epistimi.parent.adapter.mongo

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.UserId

object ParentDbBiMapper : BiMapper<Parent, ParentMongoDbDocument> {
    override fun toDomain(entity: ParentMongoDbDocument) = with(entity) {
        Parent(
            id = ParentId(id!!),
            userId = UserId(userId),
            organizationId = OrganizationId(organizationId),
        )
    }

    override fun fromDomain(domainObject: Parent) = with(domainObject) {
        ParentMongoDbDocument(
            id = id?.value,
            userId = userId.value,
            organizationId = organizationId.value,
        )
    }
}
