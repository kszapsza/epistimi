package pl.edu.wat.wcy.epistimi.teacher.domain.service

import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherRegisterRequest
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserFacade
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService.NewUser

class TeacherRegistrationService(
    private val teacherRepository: TeacherRepository,
    private val userFacade: UserFacade,
) {
    data class NewTeacher(
        val id: TeacherId? = null,
        val newUser: NewUser,
        val academicTitle: String?,
    )

    fun registerTeacher(
        contextOrganization: Organization,
        registerRequest: TeacherRegisterRequest
    ): NewTeacher {
        val teacherUser = registerTeacherUser(contextOrganization, registerRequest.user)
        val newTeacher = teacherRepository.save(
            Teacher(
                id = null,
                user = teacherUser.user,
                academicTitle = registerRequest.academicTitle,
            )
        )
        return NewTeacher(
            id = newTeacher.id,
            newUser = teacherUser,
            academicTitle = newTeacher.academicTitle,
        )
    }

    private fun registerTeacherUser(
        contextOrganization: Organization,
        userData: UserRegisterRequest,
    ): NewUser {
        return userFacade.registerUser(
            contextOrganization,
            request = userData.copy(role = UserRole.TEACHER),
        )
    }
}
