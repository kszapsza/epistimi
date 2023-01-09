package pl.edu.wat.wcy.epistimi.course.domain.port

import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId

interface CourseRepository {
    fun findById(courseId: CourseId): Course
    fun findAllWithFiltering(
        organizationId: OrganizationId,
        classTeacherId: TeacherId?,
    ): List<Course>
    fun save(course: Course): Course
}
