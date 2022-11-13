package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

data class GradeCategoryResponse(
    val id: String,
    val subject: GradeCategorySubjectResponse,
    val name: String,
    val defaultWeight: Int,
    val color: String?,
)

data class GradeCategorySubjectResponse(
    val id: String,
    val name: String,
)

data class GradeCategoriesResponse(
    val subject: GradeCategorySubjectResponse,
    val categories: List<GradeCategoriesResponseEntry>,
)

data class GradeCategoriesResponseEntry(
    val id: String,
    val name: String,
    val defaultWeight: Int,
    val color: String?,
)