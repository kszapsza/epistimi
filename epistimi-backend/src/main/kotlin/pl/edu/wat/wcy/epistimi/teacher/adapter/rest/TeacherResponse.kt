package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

data class TeacherResponse(
    val id: TeacherId? = null,
    val user: UserResponse,
    val academicTitle: String?,
)

data class TeachersResponse(
    val teachers: List<TeacherResponse>,
)
