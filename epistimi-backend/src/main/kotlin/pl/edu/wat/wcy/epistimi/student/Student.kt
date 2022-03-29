package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.user.User

data class Student(
    val id: StudentId? = null,
    val user: User,
    val organization: Organization,
    val parents: List<Parent>,
)

@JvmInline
value class StudentId(
    val value: String
)

