package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar.NewTeacher
import pl.edu.wat.wcy.epistimi.user.User

class TeacherFacade(
    private val teacherAggregator: TeacherAggregator,
    private val teacherRegistrar: TeacherRegistrar,
) {
    fun getTeachers(contextUser: User): List<Teacher> {
        return teacherAggregator.getTeachers(contextOrganization = contextUser.organization)
    }

    fun getTeacherById(
        contextUser: User,
        teacherId: TeacherId,
    ): Teacher {
        return teacherAggregator.getTeacherById(contextOrganization = contextUser.organization, teacherId)
    }

    fun registerTeacher(
        contextUser: User,
        registerRequest: TeacherRegisterRequest,
    ): NewTeacher {
        return teacherRegistrar.registerTeacher(contextOrganization = contextUser.organization, registerRequest)
    }
}
