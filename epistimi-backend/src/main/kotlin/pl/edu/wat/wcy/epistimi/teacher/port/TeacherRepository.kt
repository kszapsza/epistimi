package pl.edu.wat.wcy.epistimi.teacher.port

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId

interface TeacherRepository {
    fun findById(id: TeacherId): Teacher
    fun findByUserId(id: UserId): Teacher
    fun findAll(organizationId: OrganizationId): List<Teacher>
    fun save(teacher: Teacher): Teacher
}
