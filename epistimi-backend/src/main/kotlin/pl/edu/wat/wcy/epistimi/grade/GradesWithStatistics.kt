package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.subject.Subject
import java.math.BigDecimal

data class GradesWithStatistics(
    val gradesBySubject: List<SubjectGradesWithStatistics>,
)

data class SubjectGradesWithStatistics(
    val subject: Subject,
    val gradesByStudent: List<StudentGradesWithStatistics>,
    val statistics: GradesStatistics,
)

data class StudentGradesWithStatistics(
    val student: Student,
    val grades: List<Grade>,
    val statistics: GradesStatistics,
)

data class GradesStatistics(
    val average: GradesAverage,
)

data class GradesAverage(
    val firstSemester: BigDecimal?,
    val secondSemester: BigDecimal?,
    val overall: BigDecimal?,
)
