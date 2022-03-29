package pl.edu.wat.wcy.epistimi.course.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.course.CourseService
import pl.edu.wat.wcy.epistimi.course.dto.CoursesResponse
import pl.edu.wat.wcy.epistimi.course.dto.toCourseResponse
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.shared.api.MediaType

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseService,
) {
    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN', 'ORGANIZATION_ADMIN')")
    @RequestMapping(
        path = [""],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getOrganizations(
        @RequestParam(required = true) organizationId: OrganizationId,
    ): ResponseEntity<CoursesResponse> {
        return ResponseEntity.ok(
            CoursesResponse(
                courses = courseService.getCourses(organizationId)
                    .map { it.toCourseResponse() }
            )
        )
    }
}
