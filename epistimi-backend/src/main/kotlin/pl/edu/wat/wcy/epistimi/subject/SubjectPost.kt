package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.user.User
import java.time.LocalDateTime
import java.util.UUID

data class SubjectPost(
    val id: SubjectPostId,
    val subject: Subject,
    val author: User,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val content: String,
)

@JvmInline
value class SubjectPostId(
    val value: UUID,
)
