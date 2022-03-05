package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.user.UserId

data class Teacher(
    val id: TeacherId? = null,
    val userId: UserId,
    val academicTitle: String,
)

@JvmInline
value class TeacherId(
    val value: String
)
