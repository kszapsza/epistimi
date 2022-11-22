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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.grade.GradeFacade
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentsGradesResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectGradesResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper.GradeResponseMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper.StudentGradesResponseMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper.SubjectGradesResponseMapper
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeIssueRequest
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.net.URI
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/api")
@Tag(name = "grade", description = "API for issuing and aggregating grades")
class GradeController(
    private val gradeFacade: GradeFacade,
) {
    @Operation(
        summary = "Get grade by id",
        tags = ["grade"],
        description = "Retrieves a grade with provided id",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @GetMapping(
        path = ["/grade/{gradeId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getGrade(
        @PathVariable gradeId: GradeId,
        authentication: Authentication,
    ): ResponseEntity<GradeResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = GradeResponseMapper) {
                gradeFacade.getGradeById(
                    requester = authentication.principal as User,
                    gradeId = gradeId,
                )
            }
        )
    }

    @Operation(
        summary = "Get subject grades",
        tags = ["subject", "grade"],
        description = "Retrieves grades for provided subject id",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @GetMapping(
        path = ["/subject/{subjectId}/grade"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getSubjectGrades(
        @PathVariable subjectId: SubjectId,
        @RequestParam(required = false) studentIds: List<UUID>?,
        authentication: Authentication,
    ): ResponseEntity<SubjectGradesResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(SubjectGradesResponseMapper) {
                gradeFacade.getSubjectGrades(
                    requester = authentication.principal as User,
                    subjectId = subjectId,
                    studentIds = studentIds?.map(::StudentId),
                )
            }
        )
    }

    @Operation(
        summary = "Get student grades",
        tags = ["student", "grade"],
        description = "Retrieves grades for authorized student or authorized parent's students",
    )
    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT')")
    @GetMapping(
        path = ["/student/grade"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getStudentGrades(
        @RequestParam(name = "subjectId", required = false) subjectIds: List<UUID>?,
        authentication: Authentication,
    ): ResponseEntity<StudentsGradesResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(StudentGradesResponseMapper) {
                gradeFacade.getStudentGrades(
                    requester = authentication.principal as User,
                    subjectIds = subjectIds?.map(::SubjectId),
                )
            }
        )
    }

    @Operation(
        summary = "Issue a grade",
        tags = ["grade"],
        description = "Issues a single grade for selected student for specific course",
    )
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping(
        path = ["/grade"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun issueGrade(
        @Valid @RequestBody gradeIssueRequest: GradeIssueRequest,
        authentication: Authentication,
    ): ResponseEntity<GradeResponse> {
        return RestHandlers.handleRequest(GradeResponseMapper) {
            gradeFacade.issueGrade(
                requester = authentication.principal as User,
                gradeIssueRequest,
            )
        }.let { newGrade ->
            ResponseEntity.created(URI("/api/grade/${newGrade.id}"))
                .body(newGrade)
        }
    }

    // TODO: PUT - update grade
}
