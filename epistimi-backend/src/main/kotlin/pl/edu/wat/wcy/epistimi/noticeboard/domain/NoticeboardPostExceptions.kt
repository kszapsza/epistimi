package pl.edu.wat.wcy.epistimi.noticeboard.domain

import pl.edu.wat.wcy.epistimi.user.domain.UserId

class NoticeboardPostNotFoundException(id: NoticeboardPostId) : Exception("Noticeboard post with id ${id.value} not found")

class NoticeboardPostActionForbidden(userId: UserId?, postId: NoticeboardPostId) :
    Exception("User (id=$userId) not allowed to perform action on post (id=$postId)")
