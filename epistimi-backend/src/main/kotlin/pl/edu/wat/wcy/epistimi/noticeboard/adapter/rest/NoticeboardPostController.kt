package pl.edu.wat.wcy.epistimi.noticeboard.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostFacade
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostCreateRequest
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostId
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostUpdateRequest
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/noticeboard/post")
@Tag(name = "noticeboard/post", description = "API for managing organization noticeboard posts")
class NoticeboardPostController(
    private val noticeboardPostFacade: NoticeboardPostFacade,
) {
    @Operation(
        summary = "Get noticeboard post by id",
        tags = ["noticeboard/post"],
        description = "Retrieves a noticeboard post with provided id",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @GetMapping(
        path = ["{id}"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getNoticeboardPost(
        @PathVariable(name = "id") id: NoticeboardPostId,
        authentication: Authentication,
    ): ResponseEntity<NoticeboardPostResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = NoticeboardPostResponseMapper) {
                noticeboardPostFacade.getNoticeboardPost(
                    contextOrganization = (authentication.principal as User).organization!!,
                    noticeboardPostId = id,
                )
            }
        )
    }

    @Operation(
        summary = "Get all noticeboard posts",
        tags = ["noticeboard/post"],
        description = "Retrieves a list of all noticeboard posts (within authenticated user organization)",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getNoticeboardPosts(
        authentication: Authentication,
    ): ResponseEntity<NoticeboardPostsResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = NoticeboardPostsResponseMapper) {
                noticeboardPostFacade.getNoticeboardPosts(
                    contextOrganization = (authentication.principal as User).organization!!,
                )
            }
        )
    }

    @Operation(
        summary = "Create new noticeboard post",
        tags = ["noticeboard/post"],
        description = "Creates a new post in user's organization noticeboard",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun createNoticeboardPost(
        authentication: Authentication,
        @Valid @RequestBody createRequest: NoticeboardPostCreateRequest,
    ): ResponseEntity<NoticeboardPostResponse> {
        return RestHandlers.handleRequest(mapper = NoticeboardPostResponseMapper) {
            noticeboardPostFacade.createNoticeboardPost(
                contextUser = authentication.principal as User,
                createRequest = createRequest,
            )
        }.let { response ->
            ResponseEntity
                .created(URI("/api/noticeboard/post/${response.id!!.value}"))
                .body(response)
        }
    }

    @Operation(
        summary = "Update a noticeboard post",
        tags = ["noticeboard/post"],
        description = "Updates an existing post in user's organization noticeboard",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @PutMapping(
        path = ["{noticeboardPostId}"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun updateNoticeboardPost(
        authentication: Authentication,
        @Valid @RequestBody updateRequest: NoticeboardPostUpdateRequest,
        @PathVariable noticeboardPostId: NoticeboardPostId,
    ): ResponseEntity<NoticeboardPostResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = NoticeboardPostResponseMapper) {
                noticeboardPostFacade.updateNoticeboardPost(
                    contextUser = authentication.principal as User,
                    updateRequest = updateRequest,
                    noticeboardPostId = noticeboardPostId,
                )
            }
        )
    }

    @Operation(
        summary = "Delete a noticeboard post",
        tags = ["noticeboard/post"],
        description = "Deletes a noticeboard post with provided id",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @DeleteMapping(
        path = ["{noticeboardPostId}"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun deleteNoticeboardPost(
        authentication: Authentication,
        @PathVariable noticeboardPostId: NoticeboardPostId,
    ): ResponseEntity<Unit> {
        noticeboardPostFacade.deleteNoticeboardPost(
            contextUser = authentication.principal as User,
            noticeboardPostId = noticeboardPostId,
        )
        return ResponseEntity.noContent().build()
    }
}
