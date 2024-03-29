package pl.edu.wat.wcy.epistimi.course.adapter.rest

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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.domain.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/course")
@Tag(name = "course", description = "API for retrieving and managing courses (school classes)")
class CourseController(
    private val courseFacade: CourseFacade,
) {
    @Operation(
        summary = "Get all courses",
        tags = ["course"],
        description = "Retrieves a list of all courses (within authenticated user organization)",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getCourses(
        @RequestParam(required = false) classTeacherId: TeacherId?,
        authentication: Authentication,
    ): ResponseEntity<CoursesResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = CoursesResponseMapper) {
                courseFacade.getCourses(
                    contextUser = authentication.principal as User,
                    classTeacherId = classTeacherId,
                )
            }
        )
    }

    @Operation(
        summary = "Get course by id",
        tags = ["course"],
        description = "Returns course with provided id (if it exists in authenticated user organization)",
    )
    @PreAuthorize("hasAnyRole('ORGANIZATION_ADMIN', 'TEACHER')")
    @GetMapping(
        path = ["{courseId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getCourse(
        @PathVariable courseId: CourseId,
        authentication: Authentication,
    ): ResponseEntity<CourseResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = CourseResponseMapper) {
                courseFacade.getCourse(
                    courseId = courseId,
                    contextUser = authentication.principal as User,
                )
            }
        )
    }

    @Operation(
        summary = "Create course",
        tags = ["course"],
        description = "Creates a new course in authenticated user organization",
    )
    @PreAuthorize("hasRole('ORGANIZATION_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun createCourse(
        @Valid @RequestBody createRequest: CourseCreateRequest,
        authentication: Authentication,
    ): ResponseEntity<CourseResponse> {
        return RestHandlers.handleRequest(mapper = CourseResponseMapper) {
            courseFacade.createCourse(
                contextUser = authentication.principal as User,
                createRequest = createRequest,
            )
        }.let { createdCourse ->
            ResponseEntity
                .created(URI.create("/api/course/${createdCourse.id}"))
                .body(createdCourse)
        }
    }
}
