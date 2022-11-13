package pl.edu.wat.wcy.epistimi.noticeboard.domain.port

import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPost
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId

interface NoticeboardPostRepository {
    fun getPostById(noticeboardPostId: NoticeboardPostId): NoticeboardPost
    fun getAllPosts(organizationId: OrganizationId): List<NoticeboardPost>
    fun savePost(noticeboardPost: NoticeboardPost): NoticeboardPost
    fun deletePost(noticeboardPostId: NoticeboardPostId)
}
