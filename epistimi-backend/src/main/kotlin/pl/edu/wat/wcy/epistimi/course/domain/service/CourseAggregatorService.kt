package pl.edu.wat.wcy.epistimi.course.domain.service

import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.course.domain.CourseNotFoundException
import pl.edu.wat.wcy.epistimi.course.domain.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId

class CourseAggregatorService(
    private val courseRepository: CourseRepository,
) {
    fun getCourses(
        contextOrganization: Organization?,
        classTeacherId: TeacherId?,
    ): List<Course> {
        if (contextOrganization == null) {
            return emptyList()
        }
        return courseRepository.findAllWithFiltering(
            organizationId = contextOrganization.id!!,
            classTeacherId,
        )
    }

    fun getCourse(
        contextOrganization: Organization?,
        courseId: CourseId,
    ): Course {
        if (contextOrganization == null) {
            throw CourseNotFoundException(courseId)
        }
        val course = courseRepository.findById(courseId)
        if (course.organization.id != contextOrganization.id) {
            throw CourseNotFoundException(courseId)
        }
        return course
    }
}
