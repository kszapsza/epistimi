package pl.edu.wat.wcy.epistimi.noticeboard.adapter.sql

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPost
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostNotFoundException
import pl.edu.wat.wcy.epistimi.noticeboard.port.NoticeboardPostRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId

@Repository
class NoticeboardPostDbRepository(
    private val noticeboardPostJpaRepository: NoticeboardPostJpaRepository,
) : NoticeboardPostRepository {

    override fun getPostById(noticeboardPostId: NoticeboardPostId): NoticeboardPost {
        return DbHandlers.handleDbGet(NoticeboardPostDbBiMapper) {
            noticeboardPostJpaRepository.findById(noticeboardPostId.value)
                .orElseThrow { NoticeboardPostNotFoundException(noticeboardPostId) }
        }
    }

    override fun getAllPosts(organizationId: OrganizationId): List<NoticeboardPost> {
        // TODO: implement pagination
        return DbHandlers.handleDbMultiGet(NoticeboardPostDbBiMapper) {
            noticeboardPostJpaRepository.findAllByOrganizationId(
                organizationId = organizationId.value,
                sort = Sort.by(Sort.Direction.DESC, "createdAt"),
            )
        }
    }

    override fun savePost(noticeboardPost: NoticeboardPost): NoticeboardPost {
        return DbHandlers.handleDbInsert(
            domainObject = noticeboardPost,
            mapper = NoticeboardPostDbBiMapper,
            dbCall = noticeboardPostJpaRepository::save,
        )
    }

    override fun deletePost(noticeboardPostId: NoticeboardPostId) {
        noticeboardPostJpaRepository.deleteById(noticeboardPostId.value)
    }
}