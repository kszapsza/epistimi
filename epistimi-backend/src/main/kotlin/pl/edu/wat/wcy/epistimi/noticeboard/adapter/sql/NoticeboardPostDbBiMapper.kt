package pl.edu.wat.wcy.epistimi.noticeboard.adapter.sql

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPost
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationDbBiMapper
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserDbBiMapper

object NoticeboardPostDbBiMapper : BiMapper<NoticeboardPost, NoticeboardPostJpaEntity> {
    override fun toDomain(entity: NoticeboardPostJpaEntity) = with(entity) {
        NoticeboardPost(
            id = NoticeboardPostId(id!!),
            organization = OrganizationDbBiMapper.toDomain(organization),
            author = UserDbBiMapper.toDomain(author),
            createdAt = createdAt,
            updatedAt = updatedAt,
            title = title,
            content = content,
        )
    }

    override fun fromDomain(domainObject: NoticeboardPost) = with(domainObject) {
        NoticeboardPostJpaEntity(
            id = id?.value,
            organization = OrganizationDbBiMapper.fromDomain(organization),
            author = UserDbBiMapper.fromDomain(author),
            createdAt = createdAt,
            updatedAt = updatedAt,
            title = title,
            content = content,
        )
    }

}