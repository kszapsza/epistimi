package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.grade.port.GradeRepository
import pl.edu.wat.wcy.epistimi.subject.SubjectId

class GradeStatisticsService(
    private val gradesRepository: GradeRepository,
) {
    fun evaluateStatistics(grades: List<Grade>): GradesStatistics {
        return GradesStatistics(
            average = evaluateGradesAverage(grades),
        )
    }

    private fun evaluateGradesAverage(grades: List<Grade>): GradesAverage {
        return GradesAverage(
            firstSemester = grades.filter { it.semester == 1 }.weightedAverage(),
            secondSemester = grades.filter { it.semester == 2 }.weightedAverage(),
            overall = grades.weightedAverage(),
        )
    }

    fun evaluateSubjectStatistics(subjectId: SubjectId): GradesStatistics {
        return evaluateStatistics(grades = getGradesForSubject(subjectId))
    }

    private fun getGradesForSubject(subjectId: SubjectId): List<Grade> {
        return gradesRepository.findAllWithFiltering(
            GradeFilters(
                subjectId,
                studentIds = null,
            ),
        )
    }
}
