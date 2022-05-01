package pl.edu.wat.wcy.epistimi.teacher

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Component
class TeacherService(
    private val teacherRepository: TeacherRepository,
    private val organizationRepository: OrganizationRepository,
) {

    fun getTeachers(adminId: UserId): List<Teacher> {
        return organizationRepository.findFirstByAdminId(adminId)
            ?.let { organization -> teacherRepository.findAll(organization.id!!) }
            ?: listOf()
    }

}
