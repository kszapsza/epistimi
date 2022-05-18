package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.shared.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.user.UserId

@RestController
@RequestMapping("/api/teacher")
class TeacherController(
    private val teacherFacade: TeacherFacade,
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
            RestHandlers.handleRequest(mapper = TeachersResponseMapper) {
                teacherFacade.getTeachers(
                    userId = UserId(authentication.principal as String)
                )
            }
        )
    }

}
