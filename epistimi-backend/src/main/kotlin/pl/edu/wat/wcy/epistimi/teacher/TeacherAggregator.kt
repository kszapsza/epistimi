package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository

class TeacherAggregator(
    private val teacherRepository: TeacherRepository,
) {
    companion object {
        private val logger by logger()
    }

    fun getTeachers(contextOrganization: Organization): List<Teacher> {
        return teacherRepository.findAll(contextOrganization.id!!)
    }

    fun getTeacherById(
        contextOrganization: Organization,
        teacherId: TeacherId,
    ): Teacher {
        val teacher = teacherRepository.findById(teacherId)

        if (teacher.user.organization!!.id != contextOrganization.id) {
            logger.warn("Attempted to retrieve teacher from other organization")
            throw TeacherNotFoundException(teacherId)
        }
        return teacher
    }
}
