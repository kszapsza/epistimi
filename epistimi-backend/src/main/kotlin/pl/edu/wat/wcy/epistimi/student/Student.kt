package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.user.User
import java.util.UUID

data class Student(
    val id: StudentId? = null,
    val user: User,
    val parents: List<Parent>,
)

@JvmInline
value class StudentId(
    val value: UUID,
)
