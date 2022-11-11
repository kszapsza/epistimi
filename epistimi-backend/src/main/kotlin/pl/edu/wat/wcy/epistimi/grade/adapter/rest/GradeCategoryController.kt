package pl.edu.wat.wcy.epistimi.grade.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategoryResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper.GradeCategoryResponseMapper
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryCreateRequest
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeCategoryService
import pl.edu.wat.wcy.epistimi.user.User
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/grade/category")
@Tag(name = "grade/category", description = "API for retrieving, defining and editing grade categories")
class GradeCategoryController(
    private val gradeCategoryService: GradeCategoryService,
) {
    @Operation(
        summary = "Get grade category",
        tags = ["grade/category"],
        description = "Get grade category by provided id"
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @GetMapping(
        path = ["{id}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getGradeCategoryById(
        @PathVariable id: GradeCategoryId,
        authentication: Authentication,
    ): ResponseEntity<GradeCategoryResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(GradeCategoryResponseMapper) {
                gradeCategoryService.getCategoryById(
                    contextUser = authentication.principal as User,
                    gradeCategoryId = id,
                )
            }
        )
    }

    @Operation(
        summary = "Create grade category",
        tags = ["grade/category"],
        description = "Create grade category for subject with provided id",
    )
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun createGradeCategory(
        @Valid @RequestBody createRequest: GradeCategoryCreateRequest,
        authentication: Authentication,
    ): ResponseEntity<GradeCategoryResponse> {
        return RestHandlers.handleRequest(GradeCategoryResponseMapper) {
            gradeCategoryService.createGradeCategory(
                contextUser = authentication.principal as User,
                createRequest,
            )
        }.let { gradeCategory ->
            ResponseEntity
                .created(URI("/api/grade/category/${gradeCategory.id}"))
                .body(gradeCategory)
        }
    }
}
