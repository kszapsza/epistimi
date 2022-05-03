package pl.edu.wat.wcy.epistimi.student.dto

import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

data class StudentRegisterRequest(
    val courseId: CourseId,
    val user: UserRegisterRequest,
    val parents: List<UserRegisterRequest>,
)
