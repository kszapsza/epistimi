package pl.edu.wat.wcy.epistimi.grade.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.grade.GradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.GradeId
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradesWithStatisticsResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper.GradeResponseMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper.GradesWithStatisticsResponseMapper
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.subject.SubjectId
import pl.edu.wat.wcy.epistimi.user.User
import java.util.UUID

@RestController
@RequestMapping("/api/grade")
@Tag(name = "grade", description = "API for issuing and aggregating grades")
class GradeController(
    private val gradeAggregatorService: GradeAggregatorService,
) {
    @Operation(
        summary = "Get grade by id",
        tags = ["grade"],
        description = "Retrieves a grade with provided id",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @GetMapping(
        path = ["{gradeId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getGrade(
        @PathVariable gradeId: GradeId,
        authentication: Authentication,
    ): ResponseEntity<GradeResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = GradeResponseMapper) {
                gradeAggregatorService.getGradeById(
                    requester = authentication.principal as User,
                    gradeId = gradeId,
                )
            }
        )
    }

    @Operation(
        summary = "Get grades",
        tags = ["grade"],
        description = "Retrieves grades for provided subject id and student id(s)",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getGrades(
        @RequestParam(name = "subjectId", required = true) subjectId: UUID,
        @RequestParam(name = "studentId", required = false) studentIds: List<UUID>?,
        authentication: Authentication,
    ): ResponseEntity<GradesWithStatisticsResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(GradesWithStatisticsResponseMapper) {
                gradeAggregatorService.getGrades(
                    requester = authentication.principal as User,
                    filters = GradeFilters(SubjectId(subjectId), studentIds?.map(::StudentId)),
                )
            }
        )
    }

    /*
     * TODO:
     *   - POST /api/grade
     *      role: ORGANIZATION_ADMIN, TEACHER
     *       - wystaw ocenę (jaki student, subject, jaka ocena itd.)
     *       - tylko pozwól wystawić z przedmiotu, który żądający prowadzi!
     *       - powinno umożliwiać wystawianie tzw. seryjne (batch)
     *          - tutaj potrzebne jest trzymanie stanu na froncie i walenie batchem
     *       - jak określać, za który semestr wystawiana jest ocena? wg daty?
     *       - oceny semestralne/roczne i propozycje tych ocen
     *   Oceny niech będą na start niemutowalne. Docelowo byłby także endpoint do poprawiania ocen
     *   - wystawia nową ocenę z poprawy, unieważnia starą, wiąże je ze ze starą jako relacja "poprawa".
     *   I na start może niech nie będzie usuwania ocen.
     */
}
