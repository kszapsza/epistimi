package pl.edu.wat.wcy.epistimi.course

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository,
) {
    fun getCourses(organizationAdminId: UserId): List<Course> {
        return getOrganizationFromContext(organizationAdminId)
            ?.let { organization -> courseRepository.findAll(organization.id!!) }
            ?: listOf()
    }

    fun getOrganizationFromContext(organizationAdminId: UserId): Organization? {
        return organizationAdminId
            .let { id -> userRepository.findById(id) }
            .let { user -> organizationRepository.findAllByAdminId(user.id!!) }
            .let { organizations -> if (organizations.isEmpty()) null else organizations[0] }
    }
}
