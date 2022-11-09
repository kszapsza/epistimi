package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

data class GradesWithStatisticsResponse(
    val subjects: List<SubjectGradesWithStatisticsResponse>
)

data class SubjectGradesWithStatisticsResponse(
    val id: String,
    val name: String,
    val students: List<StudentGradesWithStatisticsResponse>,
    val statistics: GradesStatisticsResponse,
)

data class StudentGradesWithStatisticsResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val grades: List<GradeResponse>,
    val statistics: GradesStatisticsResponse,
)

data class GradesStatisticsResponse(
    val average: GradesAverageResponse,
)

data class GradesAverageResponse(
    val firstSemester: String?,
    val secondSemester: String?,
    val overall: String?,
)
