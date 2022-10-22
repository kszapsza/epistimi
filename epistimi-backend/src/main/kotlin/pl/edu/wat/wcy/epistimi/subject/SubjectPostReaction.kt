package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.user.User
import java.time.LocalDateTime
import java.util.UUID

data class SubjectPostReaction(
    val id: SubjectPostReactionId,
    val post: SubjectPost,
    val author: User,
    val type: SubjectPostReactionType = SubjectPostReactionType.NONE,
    val reactedAt: LocalDateTime? = null,
)

@JvmInline
value class SubjectPostReactionId(
    val value: UUID,
)

enum class SubjectPostReactionType {
    NONE,
    LIKE,
    LOVE,
    WOW,
    HAHA,
    SORRY,
    ANGRY;
}
