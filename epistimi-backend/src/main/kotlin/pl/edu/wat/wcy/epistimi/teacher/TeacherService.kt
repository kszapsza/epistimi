package pl.edu.wat.wcy.epistimi.teacher

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.user.UserId

@Component
class TeacherService(
    private val teacherRepository: TeacherRepository,
    private val organizationContextProvider: OrganizationContextProvider,
) {

    fun getTeachers(userId: UserId): List<Teacher> {
        return organizationContextProvider.provide(userId)
            ?.let { organization -> teacherRepository.findAll(organization.id!!) }
            ?: emptyList()
    }

}
