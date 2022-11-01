package pl.edu.wat.wcy.epistimi.noticeboard.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPost
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponseMapper

object NoticeboardPostResponseMapper : FromDomainMapper<NoticeboardPost, NoticeboardPostResponse> {
    override fun fromDomain(domainObject: NoticeboardPost) = domainObject.toNoticeboardPostResponse()
}

private fun NoticeboardPost.toNoticeboardPostResponse() = NoticeboardPostResponse(
    id = id,
    author = UserResponseMapper.fromDomain(author),
    createdDate = createdAt!!,
    updatedDate = updatedAt,
    title = title,
    content = content
)

object NoticeboardPostsResponseMapper : FromDomainMapper<List<NoticeboardPost>, NoticeboardPostsResponse> {
    override fun fromDomain(domainObject: List<NoticeboardPost>) =
        NoticeboardPostsResponse(posts = domainObject.map { it.toNoticeboardPostResponse() })
}
