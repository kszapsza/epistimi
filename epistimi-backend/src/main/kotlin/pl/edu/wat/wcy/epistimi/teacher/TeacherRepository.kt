package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.UserId

interface TeacherRepository {
    fun findById(id: TeacherId): Teacher
    fun findByUserId(id: UserId): Teacher
    fun findAll(organizationId: OrganizationId): List<Teacher>
    fun save(teacher: Teacher): Teacher
}
