package pl.edu.wat.wcy.epistimi.teacher.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import java.util.UUID

interface TeacherJpaRepository : JpaRepository<Teacher, UUID> {
    fun findFirstByUserId(userId: UUID): Teacher?
    fun findAllByUserOrganizationId(organizationId: UUID): List<Teacher>
}
