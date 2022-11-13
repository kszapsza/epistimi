package pl.edu.wat.wcy.epistimi.teacher.domain.port

import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.user.domain.UserId

interface TeacherRepository {
    fun findById(id: TeacherId): Teacher
    fun findByUserId(id: UserId): Teacher
    fun findAll(organizationId: OrganizationId): List<Teacher>
    fun save(teacher: Teacher): Teacher
}
