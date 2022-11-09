package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.grade.port.GradeRepository
import pl.edu.wat.wcy.epistimi.user.User

class GradeAggregatorService(
    private val gradeRepository: GradeRepository,
    private val gradeStatisticsService: GradeStatisticsService,
) {
    fun getGradeById(requester: User, gradeId: GradeId): Grade {
        return gradeRepository.findById(gradeId)
            .also { grade ->
                if (GradeAccessValidator.validateGradeAccess(requester, grade)) {
                    throw GradeNotFoundException(grade.id!!)
                }
            }
    }

    fun getGrades(requester: User, filters: GradeFilters): GradesWithStatistics {
        return gradeRepository.findAllWithFiltering(filters)
            .filter { grade -> GradeAccessValidator.validateGradeAccess(requester, grade) }
            .let(::getGradesWithStatistics)
    }

    private fun getGradesWithStatistics(grades: List<Grade>): GradesWithStatistics {
        return GradesWithStatistics(
            gradesBySubject = grades.groupBySubject(),
        )
    }

    private fun List<Grade>.groupBySubject(): List<SubjectGradesWithStatistics> {
        return this
            .groupBy(Grade::subject)
            .map { (subject, gradesBySubject) ->
                SubjectGradesWithStatistics(
                    subject = subject,
                    gradesByStudent = gradesBySubject.groupByStudent(),
                    statistics = gradeStatisticsService.evaluateSubjectStatistics(subject.id!!),
                )
            }
    }

    private fun List<Grade>.groupByStudent(): List<StudentGradesWithStatistics> {
        return this
            .groupBy(Grade::student)
            .map { (student, gradesByStudent) ->
                StudentGradesWithStatistics(
                    student = student,
                    grades = gradesByStudent,
                    statistics = gradeStatisticsService.evaluateStatistics(gradesByStudent),
                )
            }
    }
}
