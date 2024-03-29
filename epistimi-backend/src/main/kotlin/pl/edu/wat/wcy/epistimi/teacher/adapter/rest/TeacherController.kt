package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

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
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.net.URI
import java.util.UUID
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
                    contextUser = authentication.principal as User
                )
            }
        )
    }

    @Operation(
        summary = "Get teacher by id",
        tags = ["teacher"],
        description = "Returns a teacher with provided id (if it exists in authenticated user organization)",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN')")
    @GetMapping(
        path = ["/{teacherId}"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getTeacherById(
        authentication: Authentication,
        @PathVariable teacherId: UUID,
    ): ResponseEntity<TeacherResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = TeacherResponseMapper) {
                teacherFacade.getTeacherById(
                    contextUser = authentication.principal as User,
                    teacherId = TeacherId(teacherId),
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
                contextUser = authentication.principal as User,
                registerRequest = registerRequest,
            )
        }.let { newTeacher ->
            ResponseEntity
                .created(URI.create("/api/teacher/${newTeacher.id!!.value}"))
                .body(newTeacher)
        }
    }
}
