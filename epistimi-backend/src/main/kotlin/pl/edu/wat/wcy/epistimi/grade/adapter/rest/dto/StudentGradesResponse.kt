package pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto

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
)

data class StudentGradesSubjectSemesterResponse(
    val grades: List<GradeResponse>,
    val average: StudentGradesAverageResponse,
)

data class StudentGradesAverageResponse(
    val student: String?,
    val subject: String?,
)
