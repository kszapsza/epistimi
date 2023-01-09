package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

import java.time.LocalDateTime

data class StudentsGradesResponse(
    val students: List<StudentGradesResponse>,
)

data class StudentGradesResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val subjects: List<StudentGradesSubjectResponse>,
)

data class StudentGradesSubjectResponse(
    val id: String,
    val name: String,
    val firstSemester: StudentGradesSubjectSemesterResponse,
    val secondSemester: StudentGradesSubjectSemesterResponse,
    val average: StudentGradesAverageResponse,
    val classification: StudentSubjectClassificationResponse,
)

data class StudentSubjectClassificationResponse(
    val proposal: StudentSubjectClassificationGradeResponse?,
    val final: StudentSubjectClassificationGradeResponse?,
)

data class StudentSubjectClassificationGradeResponse(
    val id: String,
    val issuedBy: ClassificationGradeTeacherResponse,
    val issuedAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val value: ClassificationGradeValueResponse,
)

data class StudentGradesSubjectSemesterResponse(
    val grades: List<GradeResponse>,
    val average: StudentGradesAverageResponse,
    val classification: StudentSubjectClassificationResponse,
)

data class StudentGradesAverageResponse(
    val student: String?,
    val subject: String?,
)
