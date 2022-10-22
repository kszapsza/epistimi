package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.user.User
import java.time.LocalDateTime
import java.util.UUID

data class SubjectFeedPostComment(
    val id: SubjectFeedPostCommentId,
    val post: SubjectPost,
    val author: User,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val content: String,
)

@JvmInline
value class SubjectFeedPostCommentId(
    val value: UUID,
)
