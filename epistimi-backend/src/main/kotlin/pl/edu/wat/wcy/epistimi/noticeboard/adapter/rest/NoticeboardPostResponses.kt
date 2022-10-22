package pl.edu.wat.wcy.epistimi.noticeboard.adapter.rest

import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse
import java.time.LocalDateTime

data class NoticeboardPostResponse(
    val id: NoticeboardPostId? = null,
    val author: UserResponse,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime?,
    val title: String,
    val content: String,
)

data class NoticeboardPostsResponse(
    val posts: List<NoticeboardPostResponse>,
)