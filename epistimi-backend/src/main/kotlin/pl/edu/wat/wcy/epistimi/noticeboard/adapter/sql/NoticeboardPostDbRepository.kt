package pl.edu.wat.wcy.epistimi.noticeboard.adapter.sql

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPost
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostNotFoundException
import pl.edu.wat.wcy.epistimi.noticeboard.domain.port.NoticeboardPostRepository
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId

@Repository
class NoticeboardPostDbRepository(
    private val noticeboardPostJpaRepository: NoticeboardPostJpaRepository,
) : NoticeboardPostRepository {

    override fun getPostById(noticeboardPostId: NoticeboardPostId): NoticeboardPost {
        return noticeboardPostJpaRepository.findById(noticeboardPostId.value)
            .orElseThrow { NoticeboardPostNotFoundException(noticeboardPostId) }
    }

    override fun getAllPosts(organizationId: OrganizationId): List<NoticeboardPost> {
        // TODO: implement pagination
        return noticeboardPostJpaRepository.findAllByOrganizationId(
            organizationId = organizationId.value,
            sort = Sort.by(Sort.Direction.DESC, "createdAt"),
        )
    }

    override fun savePost(noticeboardPost: NoticeboardPost): NoticeboardPost {
        return noticeboardPostJpaRepository.save(noticeboardPost)
    }

    override fun deletePost(noticeboardPostId: NoticeboardPostId) {
        noticeboardPostJpaRepository.deleteById(noticeboardPostId.value)
    }
}
