package pl.edu.wat.wcy.epistimi.student.dto

import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest
import javax.validation.constraints.Size

data class StudentRegisterRequest(
    val courseId: CourseId,
    val user: UserRegisterRequest,

    @field:Size(min = 1, max = 2, message = "At least 1 and at most 2 parents required")
    val parents: List<UserRegisterRequest>,
)
