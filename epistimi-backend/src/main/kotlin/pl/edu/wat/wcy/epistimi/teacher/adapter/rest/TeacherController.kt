package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.api.MediaType
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.TeacherRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserId
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/teacher")
@Tag(name = "teacher", description = "API for retrieving and managing teachers")
class TeacherController(
    private val teacherFacade: TeacherFacade,
) {
    @Operation(
        summary = "Get all teachers",
        tags = ["teacher"],
        description = "Retrieves a list of all teachers (within authenticated user organization)",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getTeachers(
        authentication: Authentication,
    ): ResponseEntity<TeachersResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = TeachersResponseMapper) {
                teacherFacade.getTeachers(
                    userId = UserId(authentication.principal as String)
                )
            }
        )
    }

    @Operation(
        summary = "Register new teacher",
        tags = ["teacher"],
        description = "Registers new teacher in authenticated user's organization",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun registerTeacher(
        authentication: Authentication,
        @Valid @RequestBody registerRequest: TeacherRegisterRequest,
    ): ResponseEntity<TeacherRegisterResponse> {
        return RestHandlers.handleRequest(mapper = TeacherRegisterResponseMapper) {
            teacherFacade.registerTeacher(
                requesterUserId = UserId(authentication.principal as String),
                registerRequest = registerRequest,
            )
        }.let { newTeacher ->
            ResponseEntity
                .created(URI.create("/api/teacher/${newTeacher.id}"))
                .body(newTeacher)
        }
    }
}
