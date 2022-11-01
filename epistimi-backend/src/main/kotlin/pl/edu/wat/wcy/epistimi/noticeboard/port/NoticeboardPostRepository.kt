package pl.edu.wat.wcy.epistimi.noticeboard.port

import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPost
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.organization.OrganizationId

interface NoticeboardPostRepository {
    fun getPostById(noticeboardPostId: NoticeboardPostId): NoticeboardPost
    fun getAllPosts(organizationId: OrganizationId): List<NoticeboardPost>
    fun savePost(noticeboardPost: NoticeboardPost): NoticeboardPost
    fun deletePost(noticeboardPostId: NoticeboardPostId)
}
