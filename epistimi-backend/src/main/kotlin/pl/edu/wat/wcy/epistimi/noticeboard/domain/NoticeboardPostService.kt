package pl.edu.wat.wcy.epistimi.noticeboard.domain

import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.noticeboard.domain.port.NoticeboardPostRepository
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.user.UserFacade
import pl.edu.wat.wcy.epistimi.user.domain.User

class NoticeboardPostService(
    private val noticeboardPostRepository: NoticeboardPostRepository,
    private val userFacade: UserFacade,
) {
    companion object {
        private val logger by logger()
    }

    fun getNoticeboardPost(
        contextOrganization: Organization,
        noticeboardPostId: NoticeboardPostId,
    ): NoticeboardPost {
        val post = noticeboardPostRepository.getPostById(noticeboardPostId)

        if (post.organization.id != contextOrganization.id) {
            logger.warn("Attempted to retrieve noticeboard post from other organization")
            throw NoticeboardPostNotFoundException(noticeboardPostId)
        }
        return post
    }

    fun getNoticeboardPosts(contextOrganization: Organization): List<NoticeboardPost> {
        return noticeboardPostRepository.getAllPosts(contextOrganization.id!!)
    }

    fun createNoticeboardPost(
        contextUser: User,
        createRequest: NoticeboardPostCreateRequest,
    ): NoticeboardPost {
        return noticeboardPostRepository.savePost(
            NoticeboardPost(
                organization = contextUser.organization!!,
                author = userFacade.getUserById(contextUser.id!!),
                title = createRequest.title,
                content = createRequest.content,
                createdAt = null,
                updatedAt = null,
            )
        )
    }

    fun updateNoticeboardPost(
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
        updateRequest: NoticeboardPostUpdateRequest,
    ): NoticeboardPost {
        val updatedPost = getNoticeboardPost(contextUser.organization!!, noticeboardPostId)
        verifyNoticeboardPostActionAccess(updatedPost, contextUser, noticeboardPostId)
        return noticeboardPostRepository.savePost(
            NoticeboardPost(
                id = updatedPost.id,
                organization = updatedPost.organization,
                author = updatedPost.author,
                title = updateRequest.title,
                content = updateRequest.content,
                createdAt = updatedPost.createdAt,
                updatedAt = null,
            ),
        )
    }

    private fun verifyNoticeboardPostActionAccess(
        post: NoticeboardPost,
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
    ) {
        if (contextUser.id != post.author.id) {
            logger.warn("Attempted to perform action on noticeboard post created by other user")
            throw NoticeboardPostActionForbidden(contextUser.id, noticeboardPostId)
        }
    }

    fun deleteNoticeboardPost(
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
    ) {
        val post = getNoticeboardPost(contextUser.organization!!, noticeboardPostId)
        verifyNoticeboardPostActionAccess(post, contextUser, noticeboardPostId)
        noticeboardPostRepository.deletePost(noticeboardPostId)
    }
}
