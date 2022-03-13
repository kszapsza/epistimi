package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.user.UserId

data class Student(
    val id: StudentId? = null,
    val userId: UserId,
    val parentIds: List<ParentId>,

    // TODO: should student (parent, teacher) be connected to only one specific organization?
    //  if not, show to cope with students (parent, teacher) connected with potentially many various organizations?
)

@JvmInline
value class StudentId(
    val value: String
)

