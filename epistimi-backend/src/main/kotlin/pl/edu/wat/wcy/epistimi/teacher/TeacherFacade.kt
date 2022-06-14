package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar.NewTeacher
import pl.edu.wat.wcy.epistimi.user.UserId

class TeacherFacade(
    private val teacherAggregator: TeacherAggregator,
    private val teacherRegistrar: TeacherRegistrar,
) {
    fun getTeachers(requesterUserId: UserId): List<Teacher> {
        return teacherAggregator.getTeachers(requesterUserId)
    }

    fun getTeacherById(requesterUserId: UserId, teacherId: TeacherId): Teacher {
        return teacherAggregator.getTeacherById(requesterUserId, teacherId)
    }

    fun registerTeacher(
        requesterUserId: UserId,
        registerRequest: TeacherRegisterRequest
    ): NewTeacher {
        return teacherRegistrar.registerTeacher(requesterUserId, registerRequest)
    }
}
