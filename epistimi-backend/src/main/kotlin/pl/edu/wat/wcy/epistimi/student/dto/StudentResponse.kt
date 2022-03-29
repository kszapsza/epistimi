package pl.edu.wat.wcy.epistimi.student.dto

import pl.edu.wat.wcy.epistimi.parent.api.ParentResponse
import pl.edu.wat.wcy.epistimi.parent.api.toParentResponse
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse
import pl.edu.wat.wcy.epistimi.user.dto.toUserResponse

data class StudentResponse(
    val id: StudentId? = null,
    val user: UserResponse,
    val parents: List<ParentResponse>,
)

fun Student.toStudentResponse() = StudentResponse(
    id = id,
    user = user.toUserResponse(),
    parents = parents.map { it.toParentResponse() },
)
