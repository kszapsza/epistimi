package pl.edu.wat.wcy.epistimi.student.dto

import pl.edu.wat.wcy.epistimi.parent.dto.ParentResponse
import pl.edu.wat.wcy.epistimi.parent.dto.toParentResponse
import pl.edu.wat.wcy.epistimi.student.StudentDetails
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse
import pl.edu.wat.wcy.epistimi.user.dto.toUserResponse

data class StudentResponse(
    val id: StudentId? = null,
    val user: UserResponse,
    val parents: List<ParentResponse>,
)

fun StudentDetails.toStudentResponse() = StudentResponse(
    id = id,
    user = user.toUserResponse(),
    parents = parents.map { it.toParentResponse() },
)

data class StudentRegisterResponse(
    val id: StudentId? = null,
    val student: NewUserResponse,
    val parents: List<NewParentResponse>,
) {
    data class NewUserResponse(
        val user: UserResponse,
        val password: String,
    )

    data class NewParentResponse(
        val parent: ParentResponse,
        val password: String,
    )
}
