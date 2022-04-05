package pl.edu.wat.wcy.epistimi.course

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val organizationRepository: OrganizationRepository,
) {
    fun getCourses(organizationAdminId: UserId): List<Course> {
        return getOrganizationFromContext(organizationAdminId)
            ?.let { organization -> courseRepository.findAll(organization.id!!) }
            ?: listOf()
    }

    fun getOrganizationFromContext(organizationAdminId: UserId): Organization? {
        return organizationAdminId
            .let { id -> organizationRepository.findAllByAdminId(id) }
            .let { organizations -> if (organizations.isEmpty()) null else organizations[0] }
    }

    fun getCourse(courseId: CourseId, organizationAdminId: UserId): Course {
        val organizationContext = getOrganizationFromContext(organizationAdminId)
        val course = courseRepository.findById(courseId)

        if (organizationContext == null
            || course.organization.id != organizationContext.id
        ) {
            throw CourseNotFoundException(courseId)
        }
        return course
    }
}
