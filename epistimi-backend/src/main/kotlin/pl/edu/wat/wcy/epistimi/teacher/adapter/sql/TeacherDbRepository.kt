package pl.edu.wat.wcy.epistimi.teacher.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

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
