package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest

data class TeacherRegisterRequest(
    val user: UserRegisterRequest,
    val academicTitle: String?,
)
