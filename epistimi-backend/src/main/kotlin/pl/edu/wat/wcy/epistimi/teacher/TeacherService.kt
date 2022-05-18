package pl.edu.wat.wcy.epistimi.teacher

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.user.UserId

@Component
class TeacherService(
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
