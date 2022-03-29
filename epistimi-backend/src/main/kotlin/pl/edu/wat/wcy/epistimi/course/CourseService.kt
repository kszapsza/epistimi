package pl.edu.wat.wcy.epistimi.course

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.organization.OrganizationId

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    fun getCourses(organizationId: OrganizationId): List<Course> {
        return courseRepository.findAll(organizationId)
    }
}
