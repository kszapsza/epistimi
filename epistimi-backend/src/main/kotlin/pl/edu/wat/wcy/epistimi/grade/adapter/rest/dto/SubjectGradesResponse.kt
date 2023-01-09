package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

import java.time.LocalDateTime

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
    val classification: SubjectStudentClassificationResponse,
)

data class SubjectGradesStudentSemesterResponse(
    val grades: List<GradeResponse>,
    val average: String?,
    val classification: SubjectStudentClassificationResponse,
)

data class SubjectStudentClassificationResponse(
    val proposal: SubjectStudentClassificationGradeResponse?,
    val final: SubjectStudentClassificationGradeResponse?,
)

data class SubjectStudentClassificationGradeResponse(
    val id: String,
    val issuedBy: ClassificationGradeTeacherResponse,
    val issuedAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val value: ClassificationGradeValueResponse,
)

data class SubjectGradesAverageResponse(
    val firstSemester: String?,
    val secondSemester: String?,
    val overall: String?,
)
