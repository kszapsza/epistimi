package pl.edu.wat.wcy.epistimi.teacher.domain

import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import javax.validation.Valid

data class TeacherRegisterRequest(
    @Valid val user: UserRegisterRequest,
    val academicTitle: String?,
)
