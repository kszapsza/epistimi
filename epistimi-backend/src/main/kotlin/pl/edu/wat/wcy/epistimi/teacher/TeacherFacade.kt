package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

class TeacherFacade(
    private val teacherRepository: TeacherRepository,
    private val organizationContextProvider: OrganizationContextProvider,
    private val detailsDecorator: TeacherDetailsDecorator,
) {

    fun getTeachers(userId: UserId): List<TeacherDetails> {
        return organizationContextProvider.provide(userId)
            ?.let { organization -> teacherRepository.findAll(organization.id!!) }
            ?.map { teacher -> detailsDecorator.decorate(teacher) }
            ?: emptyList()
    }
}
