package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import java.math.BigDecimal

data class StudentsGrades(
    val students: List<StudentGrades>,
)

data class StudentGrades(
    val id: StudentId,
    val firstName: String,
    val lastName: String,
    val subjects: List<StudentSubjectGradesSummary>,
)

data class StudentSubjectGradesSummary(
    val id: SubjectId,
    val name: String,
    val firstSemester: StudentSubjectSemesterGradesSummary,
    val secondSemester: StudentSubjectSemesterGradesSummary,
    val average: StudentGradesAverage,
    val classification: StudentSubjectClassification,
)

data class StudentSubjectClassification(
    val proposal: ClassificationGrade?,
    val final: ClassificationGrade?,
)

data class StudentSubjectSemesterGradesSummary(
    val grades: List<Grade>,
    val average: StudentGradesAverage,
    val classification: StudentSubjectClassification,
)

data class StudentGradesAverage(
    val student: BigDecimal?,
    val subject: BigDecimal?,
)
