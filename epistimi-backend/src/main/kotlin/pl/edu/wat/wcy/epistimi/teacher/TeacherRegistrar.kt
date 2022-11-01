package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import pl.edu.wat.wcy.epistimi.user.UserRole

class TeacherRegistrar(
    private val teacherRepository: TeacherRepository,
    private val userRegistrar: UserRegistrar,
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
        return userRegistrar.registerUser(
            contextOrganization,
            request = userData.copy(role = UserRole.TEACHER),
        )
    }
}
