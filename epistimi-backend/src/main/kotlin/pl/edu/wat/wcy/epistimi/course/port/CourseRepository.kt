package pl.edu.wat.wcy.epistimi.course.port

import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

interface CourseRepository {
    fun findById(courseId: CourseId): Course
    fun findAllWithFiltering(
        organizationId: OrganizationId,
        classTeacherId: TeacherId?,
    ): List<Course>
    fun save(course: Course): Course
}
