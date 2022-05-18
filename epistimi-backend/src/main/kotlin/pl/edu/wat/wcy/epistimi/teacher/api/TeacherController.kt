package pl.edu.wat.wcy.epistimi.teacher.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.teacher.TeacherService
import pl.edu.wat.wcy.epistimi.teacher.dto.TeachersResponse
import pl.edu.wat.wcy.epistimi.teacher.dto.toTeacherResponse
import pl.edu.wat.wcy.epistimi.user.UserId

@RestController
@RequestMapping("/api/teacher")
class TeacherController(
    private val teacherService: TeacherService,
) {

    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getTeachers(
        authentication: Authentication,
    ): ResponseEntity<TeachersResponse> {
        return ResponseEntity.ok(
            TeachersResponse(
                teachers = teacherService.getTeachers(
                    userId = UserId(authentication.principal as String)
                ).map { it.toTeacherResponse() }
            )
        )
    }

}
