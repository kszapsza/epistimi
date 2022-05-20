package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

class TeacherAggregator(
    private val organizationContextProvider: OrganizationContextProvider,
    private val teacherRepository: TeacherRepository,
) {
    fun getTeachers(userId: UserId): List<Teacher> {
        return organizationContextProvider.provide(userId)
            ?.let { organization -> teacherRepository.findAll(organization.id!!) }
            ?: emptyList()
    }
}
