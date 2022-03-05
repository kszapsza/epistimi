package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.UserId

data class Student(
    val id: StudentId? = null,
    val userId: UserId,
    val parentIds: List<ParentId>,
)

@JvmInline
value class StudentId(
    val value: String
)

