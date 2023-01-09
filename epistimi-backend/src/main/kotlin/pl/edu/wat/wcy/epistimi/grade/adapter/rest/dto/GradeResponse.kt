package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

import java.time.LocalDateTime

data class GradeResponse(
    val id: String,
    val issuedBy: GradeTeacherResponse,
    val category: GradeCategorySimpleResponse,
    val semester: Int,
    val issuedAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val value: GradeValueResponse,
    val weight: Int,
    val countTowardsAverage: Boolean,
    val comment: String?,
)

data class GradeTeacherResponse(
    val id: String,
    val academicTitle: String?,
    val firstName: String,
    val lastName: String,
)

data class GradeCategorySimpleResponse(
    val id: String,
    val name: String,
    val color: String?,
)

data class GradeValueResponse(
    val displayName: String,
    val fullName: String,
)
