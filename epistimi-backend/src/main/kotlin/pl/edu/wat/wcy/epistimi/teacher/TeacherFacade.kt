package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherRegisterRequest
import pl.edu.wat.wcy.epistimi.teacher.domain.service.TeacherAggregatorService
import pl.edu.wat.wcy.epistimi.teacher.domain.service.TeacherRegistrationService
import pl.edu.wat.wcy.epistimi.teacher.domain.service.TeacherRegistrationService.NewTeacher
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId

class TeacherFacade(
    private val teacherAggregatorService: TeacherAggregatorService,
    private val teacherRegistrationService: TeacherRegistrationService,
) {
    fun getTeachers(contextUser: User): List<Teacher> {
        return teacherAggregatorService.getTeachers(contextOrganization = contextUser.organization!!)
    }

    fun getTeacherById(contextUser: User, teacherId: TeacherId): Teacher {
        return teacherAggregatorService.getTeacherById(contextUser, teacherId)
    }

    fun getTeacherByUserId(contextUser: User, userId: UserId): Teacher {
        return teacherAggregatorService.getTeacherByUserId(contextUser, userId)
    }

    fun registerTeacher(contextUser: User, registerRequest: TeacherRegisterRequest): NewTeacher {
        return teacherRegistrationService.registerTeacher(contextOrganization = contextUser.organization!!, registerRequest)
    }
}
