package pl.edu.wat.wcy.epistimi.noticeboard

import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPost
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostCreateRequest
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostService
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostUpdateRequest
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.user.domain.User

class NoticeboardPostFacade(
    private val noticeboardPostService: NoticeboardPostService,
) {
    fun getNoticeboardPost(
        contextOrganization: Organization,
        noticeboardPostId: NoticeboardPostId,
    ): NoticeboardPost {
        return noticeboardPostService.getNoticeboardPost(contextOrganization, noticeboardPostId)
    }

    fun getNoticeboardPosts(contextOrganization: Organization): List<NoticeboardPost> {
        return noticeboardPostService.getNoticeboardPosts(contextOrganization)
    }

    fun createNoticeboardPost(
        contextUser: User,
        createRequest: NoticeboardPostCreateRequest,
    ): NoticeboardPost {
        return noticeboardPostService.createNoticeboardPost(contextUser, createRequest)
    }

    fun updateNoticeboardPost(
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
        updateRequest: NoticeboardPostUpdateRequest,
    ): NoticeboardPost {
        return noticeboardPostService.updateNoticeboardPost(contextUser, noticeboardPostId, updateRequest)
    }

    fun deleteNoticeboardPost(
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
    ) {
        return noticeboardPostService.deleteNoticeboardPost(contextUser, noticeboardPostId)
    }
}