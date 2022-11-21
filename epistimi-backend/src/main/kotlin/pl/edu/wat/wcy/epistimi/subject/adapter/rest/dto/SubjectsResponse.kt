package pl.edu.wat.wcy.epistimi.subject.adapter.rest.dto

import pl.edu.wat.wcy.epistimi.user.domain.UserRole

data class SubjectsResponse(
    val schoolYears: List<SchoolYearSubjectsResponse>,
)

data class SchoolYearSubjectsResponse(
    val schoolYear: String,
    val courses: List<CourseSubjectsResponse>
)

data class CourseSubjectsResponse(
    val courseId: String,
    val code: String,
    val classTeacher: SubjectsTeacherResponse,
    val studentsCount: Int,
    val subjects: List<SubjectsEntryResponse>,
)

data class SubjectsTeacherResponse(
    val id: String,
    val academicTitle: String?,
    val user: SubjectsUserResponse,
)

data class SubjectsUserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val username: String,
)

data class SubjectsEntryResponse(
    val id: String,
    val teacher: SubjectsTeacherResponse,
    val name: String,
)