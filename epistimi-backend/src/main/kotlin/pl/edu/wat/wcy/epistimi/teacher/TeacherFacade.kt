package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar.NewTeacher
import pl.edu.wat.wcy.epistimi.user.UserId

class TeacherFacade(
    private val teacherAggregator: TeacherAggregator,
    private val teacherRegistrar: TeacherRegistrar,
    private val teacherDetailsDecorator: TeacherDetailsDecorator,
) {
    fun getTeachers(requesterUserId: UserId): List<TeacherDetails> {
        return teacherAggregator.getTeachers(requesterUserId)
            .map { teacher -> teacherDetailsDecorator.decorate(teacher) }
    }

    fun getTeacherById(requesterUserId: UserId, teacherId: TeacherId): TeacherDetails {
        return teacherAggregator.getTeacherById(requesterUserId, teacherId)
            .let { teacher -> teacherDetailsDecorator.decorate(teacher) }
    }

    fun registerTeacher(
        requesterUserId: UserId,
        registerRequest: TeacherRegisterRequest
    ): NewTeacher {
        return teacherRegistrar.registerTeacher(requesterUserId, registerRequest)
    }
}
