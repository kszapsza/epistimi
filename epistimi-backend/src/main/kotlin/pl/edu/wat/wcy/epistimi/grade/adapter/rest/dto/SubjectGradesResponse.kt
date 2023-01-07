package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

data class SubjectGradesResponse(
    val id: String,
    val name: String,
    val students: List<SubjectGradesStudentResponse>,
    val average: SubjectGradesAverageResponse,
)

data class SubjectGradesStudentResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val firstSemester: SubjectGradesStudentSemesterResponse,
    val secondSemester: SubjectGradesStudentSemesterResponse,
    val average: String?,
)

data class SubjectGradesStudentSemesterResponse(
    val grades: List<GradeResponse>,
    val average: String?,
)

data class SubjectGradesAverageResponse(
    val firstSemester: String?,
    val secondSemester: String?,
    val overall: String?,
)
