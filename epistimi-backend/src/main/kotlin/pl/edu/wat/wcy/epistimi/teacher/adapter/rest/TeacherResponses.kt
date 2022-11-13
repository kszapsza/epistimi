package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

data class TeacherResponse(
    val id: TeacherId? = null,
    val user: UserResponse,
    val academicTitle: String?,
)

data class TeachersResponse(
    val teachers: List<TeacherResponse>,
)

data class TeacherRegisterResponse(
    val id: TeacherId? = null,
    val newUser: NewUserResponse,
    val academicTitle: String?,
) {
    data class NewUserResponse(
        val user: UserResponse,
        val password: String,
    )
}
