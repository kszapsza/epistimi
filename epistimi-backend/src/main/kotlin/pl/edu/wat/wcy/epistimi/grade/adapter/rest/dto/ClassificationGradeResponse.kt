package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGradePeriod
import java.time.LocalDateTime

data class ClassificationGradeResponse(
    val id: String,
    val subject: ClassificationGradeSubjectResponse,
    val student: ClassificationGradeStudentResponse,
    val issuedBy: ClassificationGradeTeacherResponse,
    val issuedAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val value: ClassificationGradeValueResponse,
    val period: ClassificationGradePeriod,
    val isProposal: Boolean,
)

data class ClassificationGradeSubjectResponse(
    val id: String,
    val name: String,
    val course: ClassificationGradeCourseResponse,
)

data class ClassificationGradeCourseResponse(
    val id: String,
    val code: String,
    val schoolYear: String,
)

data class ClassificationGradeStudentResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
)

data class ClassificationGradeTeacherResponse(
    val id: String,
    val academicTitle: String?,
    val firstName: String,
    val lastName: String,
)

data class ClassificationGradeValueResponse(
    val displayName: String,
    val fullName: String,
)
