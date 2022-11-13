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
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.student.domain.StudentRegisterRequest
import pl.edu.wat.wcy.epistimi.student.domain.service.StudentRegistrationService
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/student")
@Tag(name = "student", description = "API for retrieving and managing students")
class StudentController(
    private val studentRegistrationService: StudentRegistrationService,
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
            studentRegistrationService.registerStudent(
                contextUser = (authentication.principal as User),
                request = studentRegisterRequest,
            )
        }.let { newStudent ->
            ResponseEntity
                .created(URI.create("/api/organization/${newStudent.id!!.value}"))
                .body(newStudent)
        }
    }
}
