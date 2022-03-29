package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.organization.OrganizationId

interface CourseRepository {
    fun findAll(organizationId: OrganizationId): List<Course>
    fun save(course: Course): Course
}
