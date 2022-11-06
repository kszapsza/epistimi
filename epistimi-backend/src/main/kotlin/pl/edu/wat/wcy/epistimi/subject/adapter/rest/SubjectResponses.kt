package pl.edu.wat.wcy.epistimi.subject.adapter.rest

import pl.edu.wat.wcy.epistimi.user.UserRole

data class SubjectResponse(
    val id: String,
    val course: SubjectCourseResponse,
    val teacher: SubjectTeacherResponse,
    val name: String,
)

data class SubjectCourseResponse(
    val id: String,
    val code: Code,
    val schoolYear: String,
    val classTeacher: SubjectTeacherResponse,
    val students: List<SubjectStudentResponse>,
) {
    data class Code(
        val number: String,
        val letter: String,
    )
}

data class SubjectTeacherResponse(
    val id: String,
    val academicTitle: String?,
    val user: SubjectUserResponse,
)

data class SubjectUserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val username: String,
)

data class SubjectStudentResponse(
    val id: String,
    val user: SubjectUserResponse,
)
