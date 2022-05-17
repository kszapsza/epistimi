package pl.edu.wat.wcy.epistimi.teacher.dto

import pl.edu.wat.wcy.epistimi.teacher.TeacherDetails
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse
import pl.edu.wat.wcy.epistimi.user.dto.toUserResponse

data class TeacherResponse(
    val id: TeacherId? = null,
    val user: UserResponse,
    val academicTitle: String?,
)

fun TeacherDetails.toTeacherResponse() = TeacherResponse(
    id = id,
    user = user.toUserResponse(),
    academicTitle = academicTitle,
)

data class TeachersResponse(
    val teachers: List<TeacherResponse>,
)
