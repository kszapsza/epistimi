package pl.edu.wat.wcy.epistimi.noticeboard

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.user.User
import java.time.LocalDateTime
import java.util.UUID

data class NoticeboardPost(
    val id: NoticeboardPostId? = null,
    val organization: Organization,
    val author: User,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val title: String,
    val content: String,
)

@JvmInline
value class NoticeboardPostId(
    val value: UUID,
)
