package pl.edu.wat.wcy.epistimi.student.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.parent.dto.toParentResponse
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.student.StudentRegistrar
import pl.edu.wat.wcy.epistimi.student.StudentRegistrar.NewStudent
import pl.edu.wat.wcy.epistimi.student.dto.StudentRegisterRequest
import pl.edu.wat.wcy.epistimi.student.dto.StudentRegisterResponse
import pl.edu.wat.wcy.epistimi.student.dto.StudentRegisterResponse.NewParentResponse
import pl.edu.wat.wcy.epistimi.student.dto.StudentRegisterResponse.NewUserResponse
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.dto.toUserResponse
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/student")
class StudentController(
    private val studentRegistrar: StudentRegistrar,
) {
    @PreAuthorize("hasRole('ORGANIZATION_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun registerStudent(
        authentication: Authentication,
        @Valid @RequestBody studentRegisterRequest: StudentRegisterRequest,
    ): ResponseEntity<StudentRegisterResponse> {
        val newStudent = studentRegistrar.registerStudent(
            requesterUserId = UserId(authentication.principal as String),
            request = studentRegisterRequest,
        )
        return ResponseEntity
            .created(URI.create("/api/organization/${newStudent.id}"))
            .body(newStudent.toResponse())
    }

    private fun NewStudent.toResponse() = StudentRegisterResponse(
        id = id,
        student = user.let {
            NewUserResponse(
                user = it.user.toUserResponse(),
                password = it.password
            )
        },
        parents = parents.map {
            NewParentResponse(
                parent = it.parent.toParentResponse(),
                password = it.password
            )
        }
    )
}
