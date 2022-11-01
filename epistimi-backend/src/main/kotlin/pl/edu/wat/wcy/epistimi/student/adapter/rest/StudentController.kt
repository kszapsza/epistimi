package pl.edu.wat.wcy.epistimi.student.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.api.MediaType
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.student.StudentRegisterRequest
import pl.edu.wat.wcy.epistimi.student.StudentRegistrar
import pl.edu.wat.wcy.epistimi.user.User
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/student")
@Tag(name = "student", description = "API for retrieving and managing students")
class StudentController(
    private val studentRegistrar: StudentRegistrar,
) {
    @Operation(
        summary = "Register student",
        tags = ["student"],
        description = "Registers a new student within a course with provided id",
    )
    @PreAuthorize("hasRole('ORGANIZATION_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun registerStudent(
        authentication: Authentication,
        @Valid @RequestBody studentRegisterRequest: StudentRegisterRequest,
    ): ResponseEntity<StudentRegisterResponse> {
        return RestHandlers.handleRequest(mapper = StudentRegisterResponseMapper) {
            studentRegistrar.registerStudent(
                contextOrganization = (authentication.principal as User).organization,
                request = studentRegisterRequest,
            )
        }.let { newStudent ->
            ResponseEntity
                .created(URI.create("/api/organization/${newStudent.id}"))
                .body(newStudent)
        }
    }
}
