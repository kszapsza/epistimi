package pl.edu.wat.wcy.epistimi.grade.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.grade.ClassificationGradeFacade
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper.ClassificationGradeResponseMapper
import pl.edu.wat.wcy.epistimi.grade.domain.request.ClassificationGradeIssueRequest
import pl.edu.wat.wcy.epistimi.user.domain.User
import javax.validation.Valid

@RestController
@RequestMapping("/api/grade/classification")
@Tag(name = "grade/classification", description = "API for issuing classification grades and their proposals")
class ClassificationGradeController(
    private val classificationGradeFacade: ClassificationGradeFacade,
) {
    @Operation(
        summary = "Issue classification grade (or its proposal)",
        tags = ["grade/classification"],
        description = "Issues new classification grade (or its proposal) for given student and subject"
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun issueClassificationGrade(
        authentication: Authentication,
        @Valid @RequestBody issueRequest: ClassificationGradeIssueRequest,
    ): ResponseEntity<ClassificationGradeResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(ClassificationGradeResponseMapper) {
                classificationGradeFacade.issueClassificationGrade(
                    contextUser = authentication.principal as User,
                    issueRequest = issueRequest,
                )
            }
        )
    }
}
