package pl.edu.wat.wcy.epistimi.noticeboard

import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.noticeboard.port.NoticeboardPostRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

class NoticeboardPostService(
    private val noticeboardPostRepository: NoticeboardPostRepository,
    private val userRepository: UserRepository,
    private val organizationContextProvider: OrganizationContextProvider,
) {
    companion object {
        private val logger by logger()
    }

    private fun getNoticeboardPost(
        requesterUserId: UserId,
        noticeboardPostId: NoticeboardPostId
    ): NoticeboardPost {
        val contextOrganization = organizationContextProvider.provide(requesterUserId)
        val post = noticeboardPostRepository.getPostById(noticeboardPostId)

        if (contextOrganization == null || post.organization.id != contextOrganization.id) {
            logger.warn("Attempted to retrieve noticeboard post from other organization")
            throw NoticeboardPostNotFoundException(noticeboardPostId)
        }
        return post
    }

    fun getNoticeboardPosts(requesterUserId: UserId): List<NoticeboardPost> {
        return organizationContextProvider.provide(requesterUserId)
            ?.let { organization -> noticeboardPostRepository.getAllPosts(organization.id!!) }
            ?: emptyList()
    }

    fun createNoticeboardPost(
        requesterUserId: UserId,
        createRequest: NoticeboardPostCreateRequest,
    ): NoticeboardPost {
        return noticeboardPostRepository.savePost(
            NoticeboardPost(
                organization = organizationContextProvider.provide(requesterUserId)!!,
                author = userRepository.findById(requesterUserId),
                title = createRequest.title,
                content = createRequest.content,
            )
        )
    }

    fun updateNoticeboardPost(
        requesterUserId: UserId,
        noticeboardPostId: NoticeboardPostId,
        updateRequest: NoticeboardPostUpdateRequest,
    ): NoticeboardPost {
        val post = getNoticeboardPost(requesterUserId, noticeboardPostId)
        verifyNoticeboardPostActionAccess(post, requesterUserId, noticeboardPostId)
        return noticeboardPostRepository.savePost(
            post.copy(
                title = updateRequest.title,
                content = updateRequest.content,
            )
        )
    }

    private fun verifyNoticeboardPostActionAccess(
        post: NoticeboardPost,
        requesterUserId: UserId,
        noticeboardPostId: NoticeboardPostId
    ) {
        if (requesterUserId != post.author.id) {
            logger.warn("Attempted to perform action on noticeboard post created by other user")
            throw NoticeboardPostActionForbidden(requesterUserId, noticeboardPostId)
        }
    }

    fun deleteNoticeboardPost(
        requesterUserId: UserId,
        noticeboardPostId: NoticeboardPostId,
    ) {
        val post = getNoticeboardPost(requesterUserId, noticeboardPostId)
        verifyNoticeboardPostActionAccess(post, requesterUserId, noticeboardPostId)
        noticeboardPostRepository.deletePost(noticeboardPostId)
    }
}