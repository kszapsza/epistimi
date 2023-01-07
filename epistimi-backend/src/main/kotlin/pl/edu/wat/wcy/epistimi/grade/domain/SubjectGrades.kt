package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import java.math.BigDecimal

data class SubjectGrades(
    val id: SubjectId,
    val name: String,
    val students: List<SubjectStudentGradesSummary>,
    val average: SubjectGradesAverageSummary,
)

data class SubjectStudentGradesSummary(
    val id: StudentId,
    val firstName: String,
    val lastName: String,
    val firstSemester: SubjectStudentSemesterGradesSummary,
    val secondSemester: SubjectStudentSemesterGradesSummary,
    val average: BigDecimal?,
)

data class SubjectStudentClassification(
    val proposal: ClassificationGrade?,
    val final: ClassificationGrade?,
)

data class SubjectStudentSemesterGradesSummary(
    val grades: List<Grade>,
    val average: BigDecimal?,
)

data class SubjectGradesAverageSummary(
    val firstSemester: BigDecimal?,
    val secondSemester: BigDecimal?,
    val overall: BigDecimal?,
)
