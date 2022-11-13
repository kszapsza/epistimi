package pl.edu.wat.wcy.epistimi.subject.adapter.rest

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
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/subject")
@Tag(name = "subject", description = "API for managing subjects within courses (classes)")
class SubjectController(
    private val subjectFacade: SubjectFacade,
) {
    @Operation(
        summary = "Get subject",
        tags = ["subject"],
        description = "Retrieves subject by provided id",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @GetMapping(
        path = ["{id}"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getSubjectById(
        @PathVariable id: SubjectId,
        authentication: Authentication,
    ): ResponseEntity<SubjectResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = SubjectResponseMapper) {
                subjectFacade.getSubject(
                    contextUser = authentication.principal as User,
                    subjectId = id,
                )
            }
        )
    }

    /*
     * TODO:
     *  On frontend we will need "multi" GET endpoint, returning subjects
     *  within users' organization AND:
     *   - for ORGANIZATION_ADMIN / TEACHER: subjects led by context teacher
     *   - for STUDENT: subjects for courses attended by student
     *   - for PARENT: subjects for courses attended by parent's child
     *   - for EPISTIMI_ADMIN: not applicable
     */

    @Operation(
        summary = "Register subject",
        tags = ["subject"],
        description = "Registers new subject within provided course and with provided teacher",
    )
    @PreAuthorize("hasRole('ORGANIZATION_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun registerSubject(
        @Valid @RequestBody subjectRegisterRequest: SubjectRegisterRequest,
        authentication: Authentication,
    ): ResponseEntity<SubjectResponse> {
        return RestHandlers.handleRequest(mapper = SubjectResponseMapper) {
            subjectFacade.registerSubject(
                contextUser = authentication.principal as User,
                subjectRegisterRequest = subjectRegisterRequest,
            )
        }.let { newSubject ->
            ResponseEntity
                .created(URI.create("/api/subject/${newSubject.id}"))
                .body(newSubject)
        }
    }
}
