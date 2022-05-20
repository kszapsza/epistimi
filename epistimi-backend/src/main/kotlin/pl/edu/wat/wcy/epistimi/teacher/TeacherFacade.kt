package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar.NewTeacher
import pl.edu.wat.wcy.epistimi.user.UserId

class TeacherFacade(
    private val teacherAggregator: TeacherAggregator,
    private val teacherRegistrar: TeacherRegistrar,
    private val teacherDetailsDecorator: TeacherDetailsDecorator,
) {
    fun getTeachers(userId: UserId): List<TeacherDetails> {
        return teacherAggregator.getTeachers(userId)
            .map { teacher -> teacherDetailsDecorator.decorate(teacher) }
    }

    fun registerTeacher(
        requesterUserId: UserId,
        registerRequest: TeacherRegisterRequest
    ): NewTeacher {
        return teacherRegistrar.registerTeacher(requesterUserId, registerRequest)
    }
}
