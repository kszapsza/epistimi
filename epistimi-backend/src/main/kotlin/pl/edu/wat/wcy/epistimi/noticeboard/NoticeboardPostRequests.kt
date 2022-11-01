package pl.edu.wat.wcy.epistimi.noticeboard

import javax.validation.constraints.NotBlank

data class NoticeboardPostCreateRequest(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val content: String,
)

data class NoticeboardPostUpdateRequest(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val content: String,
)
