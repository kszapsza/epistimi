package pl.edu.wat.wcy.epistimi.teacher.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.domain.UserId

@Repository
class TeacherDbRepository(
    private val teacherJpaRepository: TeacherJpaRepository,
) : TeacherRepository {

    override fun findById(id: TeacherId): Teacher {
        return teacherJpaRepository.findById(id.value)
            .orElseThrow { TeacherNotFoundException(id) }
    }

    override fun findByUserId(id: UserId): Teacher {
        return teacherJpaRepository.findFirstByUserId(id.value)
            ?: throw TeacherNotFoundException()
    }

    override fun findAll(organizationId: OrganizationId): List<Teacher> {
        return teacherJpaRepository.findAllByUserOrganizationId(organizationId.value)
    }

    override fun save(teacher: Teacher): Teacher {
        return teacherJpaRepository.save(teacher)
    }
}
