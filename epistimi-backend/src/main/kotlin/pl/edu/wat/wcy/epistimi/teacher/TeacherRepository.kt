package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.OrganizationId

interface TeacherRepository {
    fun findById(id: TeacherId): Teacher
    fun findAll(organizationId: OrganizationId): List<Teacher>
    fun save(teacher: Teacher): Teacher
}
