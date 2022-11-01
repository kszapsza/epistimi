package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

class CourseAggregator(
    private val courseRepository: CourseRepository,
) {
    fun getCourses(
        contextOrganization: Organization,
        classTeacherId: TeacherId?,
    ): List<Course> {
        return courseRepository.findAllWithFiltering(
            organizationId = contextOrganization.id!!,
            classTeacherId,
        )
    }

    fun getCourse(
        contextOrganization: Organization,
        courseId: CourseId,
    ): Course {
        val course = courseRepository.findById(courseId)

        if (course.organization.id != contextOrganization.id) {
            throw CourseNotFoundException(courseId)
        }
        return course
    }
}
