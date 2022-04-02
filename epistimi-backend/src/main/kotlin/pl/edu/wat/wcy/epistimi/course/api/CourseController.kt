package pl.edu.wat.wcy.epistimi.course.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.course.CourseService
import pl.edu.wat.wcy.epistimi.course.dto.CoursesResponse
import pl.edu.wat.wcy.epistimi.course.dto.toCourseResponse
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.user.UserId

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseService,
) {
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN')")
    @RequestMapping(
        path = [""],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getCourses(
        authentication: Authentication
    ): ResponseEntity<CoursesResponse> {
        return ResponseEntity.ok(
            CoursesResponse(
                courses = courseService.getCourses(UserId(authentication.principal as String))
                    .map { it.toCourseResponse() }
            )
        )
    }

    // TODO: single GET endpoint should return HTTP 403 if course is in other organization!
}
