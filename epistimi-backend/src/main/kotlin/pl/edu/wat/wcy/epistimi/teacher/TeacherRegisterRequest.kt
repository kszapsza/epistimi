package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import javax.validation.Valid

data class TeacherRegisterRequest(
    @Valid val user: UserRegisterRequest, // TODO: consistent validation on frontend!
    val academicTitle: String?,
)
