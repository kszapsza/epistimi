package pl.edu.wat.wcy.epistimi.student.adapter.rest

import pl.edu.wat.wcy.epistimi.parent.adapter.rest.ParentResponse
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

data class StudentResponse(
    val id: StudentId? = null,
    val user: UserResponse,
    val parents: List<ParentResponse>,
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
