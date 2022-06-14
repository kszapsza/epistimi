package pl.edu.wat.wcy.epistimi.parent.adapter.sql

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationDbBiMapper
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserDbBiMapper

object ParentDbBiMapper : BiMapper<Parent, ParentJpaEntity> {
    override fun toDomain(entity: ParentJpaEntity) = with(entity) {
        Parent(
            id = ParentId(id!!),
            user = UserDbBiMapper.toDomain(user),
            organization = OrganizationDbBiMapper.toDomain(organization),
        )
    }

    override fun fromDomain(domainObject: Parent) = with(domainObject) {
        ParentJpaEntity(
            id = id?.value,
            user = UserDbBiMapper.fromDomain(user),
            organization = OrganizationDbBiMapper.fromDomain(organization),
        )
    }
}
