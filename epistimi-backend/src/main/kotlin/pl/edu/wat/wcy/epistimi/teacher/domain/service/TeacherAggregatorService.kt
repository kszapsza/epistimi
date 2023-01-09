package pl.edu.wat.wcy.epistimi.teacher.domain.service

import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherUserIdNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.domain.access.TeacherAccessValidator
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId

class TeacherAggregatorService(
    private val teacherRepository: TeacherRepository,
    private val teacherAccessValidator: TeacherAccessValidator,
) {
    companion object {
        private val logger by logger()
    }

    fun getTeachers(contextOrganization: Organization): List<Teacher> {
        return teacherRepository.findAll(contextOrganization.id!!)
    }

    fun getTeacherById(contextUser: User, teacherId: TeacherId): Teacher {
        return teacherRepository.findById(teacherId)
            .also { teacher ->
                if (!teacherAccessValidator.canRetrieve(contextUser, teacher)) {
                    logger.warn("Unauthorized teacher fetch attempted (requester userId=${contextUser.id!!.value}, teacherId=$teacherId")
                    throw TeacherNotFoundException(teacherId)
                }
            }
    }

    fun getTeacherByUserId(contextUser: User, userId: UserId): Teacher {
        return teacherRepository.findByUserId(userId)
            .also { teacher ->
                if (!teacherAccessValidator.canRetrieve(contextUser, teacher)) {
                    logger.warn("Unauthorized teacher fetch attempted (requester userId=${contextUser.id!!.value}, teacher userId=$userId")
                    throw TeacherUserIdNotFoundException(userId)
                }
            }
    }
}
