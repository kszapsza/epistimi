package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

class TeacherAggregator(
    private val organizationContextProvider: OrganizationContextProvider,
    private val teacherRepository: TeacherRepository,
) {
    companion object {
        private val logger by logger()
    }

    fun getTeachers(requesterUserId: UserId): List<Teacher> {
        return organizationContextProvider.provide(requesterUserId)
            ?.let { organization -> teacherRepository.findAll(organization.id!!) }
            ?: emptyList()
    }

    fun getTeacherById(requesterUserId: UserId, teacherId: TeacherId): Teacher {
        val requesterOrganization = organizationContextProvider.provide(requesterUserId)
        val teacher = teacherRepository.findById(teacherId)

        if (requesterOrganization == null || teacher.organization.id != requesterOrganization.id) {
            logger.warn("Attempted to retrieve teacher from other organization")
            throw TeacherNotFoundException(teacherId)
        }
        return teacher
    }
}
