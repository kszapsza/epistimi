package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.student.StudentBadRequestException
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser

class TeacherRegistrar(
    private val teacherRepository: TeacherRepository,
    private val userRegistrar: UserRegistrar,
    private val organizationContextProvider: OrganizationContextProvider,
) {
    data class NewTeacher(
        val id: TeacherId? = null,
        val newUser: NewUser,
        val academicTitle: String?,
    )

    fun registerTeacher(
        requesterUserId: UserId,
        registerRequest: TeacherRegisterRequest
    ): NewTeacher {
        val organization = organizationContextProvider.provide(requesterUserId)
            ?: throw StudentBadRequestException("User not managing any organization")

        val teacherUser = registerTeacherUser(registerRequest.user)
        val newTeacher = teacherRepository.save(
            Teacher(
                id = null,
                user = teacherUser.user,
                organization = organization,
                academicTitle = registerRequest.academicTitle,
            )
        )

        return NewTeacher(
            id = newTeacher.id,
            newUser = teacherUser,
            academicTitle = newTeacher.academicTitle,
        )
    }

    private fun registerTeacherUser(userData: UserRegisterRequest): NewUser {
        return userRegistrar.registerUser(
            userData.copy(role = User.Role.TEACHER)
        )
    }
}
