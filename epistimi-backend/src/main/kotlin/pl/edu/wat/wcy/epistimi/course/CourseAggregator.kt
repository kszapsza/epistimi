package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId

class CourseAggregator(
    private val courseRepository: CourseRepository,
    private val organizationContextProvider: OrganizationContextProvider,
) {
    fun getCourses(
        requesterUserId: UserId,
        classTeacherId: TeacherId?,
    ): List<Course> {
        return organizationContextProvider.provide(requesterUserId)
            ?.let { contextOrganization -> courseRepository.findAllWithFiltering(contextOrganization.id!!, classTeacherId) }
            ?: emptyList()
    }

    fun getCourse(courseId: CourseId, userId: UserId): Course {
        val contextOrganization = organizationContextProvider.provide(userId)
        val course = courseRepository.findById(courseId)

        if (contextOrganization == null || course.organization.id != contextOrganization.id) {
            throw CourseNotFoundException(courseId)
        }
        return course
    }
}
