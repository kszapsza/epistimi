package pl.edu.wat.wcy.epistimi.noticeboard

import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.noticeboard.port.NoticeboardPostRepository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

class NoticeboardPostService(
    private val noticeboardPostRepository: NoticeboardPostRepository,
    private val userRepository: UserRepository,
) {
    companion object {
        private val logger by logger()
    }

    private fun getNoticeboardPost(
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
                organization = contextUser.organization,
                author = userRepository.findById(contextUser.id!!),
                title = createRequest.title,
                content = createRequest.content,
            )
        )
    }

    fun updateNoticeboardPost(
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
        updateRequest: NoticeboardPostUpdateRequest,
    ): NoticeboardPost {
        val post = getNoticeboardPost(contextUser.organization, noticeboardPostId)
        verifyNoticeboardPostActionAccess(post, contextUser, noticeboardPostId)
        return noticeboardPostRepository.savePost(
            post.copy(
                title = updateRequest.title,
                content = updateRequest.content,
            )
        )
    }

    private fun verifyNoticeboardPostActionAccess(
        post: NoticeboardPost,
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
    ) {
        // TODO: organization admin can update/delete everything?
        if (contextUser.id != post.author.id) {
            logger.warn("Attempted to perform action on noticeboard post created by other user")
            throw NoticeboardPostActionForbidden(contextUser.id, noticeboardPostId)
        }
    }

    fun deleteNoticeboardPost(
        contextUser: User,
        noticeboardPostId: NoticeboardPostId,
    ) {
        val post = getNoticeboardPost(contextUser.organization, noticeboardPostId)
        verifyNoticeboardPostActionAccess(post, contextUser, noticeboardPostId)
        noticeboardPostRepository.deletePost(noticeboardPostId)
    }
}
