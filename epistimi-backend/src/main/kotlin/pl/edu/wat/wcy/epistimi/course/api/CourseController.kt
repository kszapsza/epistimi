package pl.edu.wat.wcy.epistimi.course.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseService
import pl.edu.wat.wcy.epistimi.course.dto.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.dto.CourseResponse
import pl.edu.wat.wcy.epistimi.course.dto.CoursesResponse
import pl.edu.wat.wcy.epistimi.course.dto.toCourseResponse
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.user.UserId
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseService,
) {
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
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

    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @RequestMapping(
        path = ["{courseId}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getCourse(
        @PathVariable courseId: CourseId,
        authentication: Authentication,
    ): ResponseEntity<CourseResponse> {
        return ResponseEntity.ok(
            courseService.getCourse(
                courseId = courseId,
                userId = UserId(authentication.principal as String),
            ).toCourseResponse()
        )
    }

    @PreAuthorize("hasRole('ORGANIZATION_ADMIN')")
    @RequestMapping(
        path = [""],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun createCourse(
        @Valid @RequestBody createRequest: CourseCreateRequest,
        authentication: Authentication,
    ): ResponseEntity<CourseResponse> {
        return courseService.createCourse(
            userId = UserId(authentication.principal as String),
            createRequest = createRequest,
        ).let {
            ResponseEntity
                .created(URI("/api/course/${it.id!!.value}"))
                .body(it.toCourseResponse())
        }
    }
}
