package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.GradesWithStatistics
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGradesWithStatistics
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGradesWithStatistics
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.user.domain.User

class GradeAggregatorService(
    private val gradeRepository: GradeRepository,
    private val gradeStatisticsService: GradeStatisticsService,
    private val gradeAccessValidator: GradeAccessValidator,
) {
    fun getGradeById(requester: User, gradeId: GradeId): Grade {
        return gradeRepository.findById(gradeId)
            .also { grade ->
                if (gradeAccessValidator.canRetrieve(requester, grade)) {
                    throw GradeNotFoundException(grade.id!!)
                }
            }
    }

    fun getGrades(requester: User, filters: GradeFilters): GradesWithStatistics {
        return gradeRepository.findAllWithFiltering(filters)
            .filter { grade -> gradeAccessValidator.canRetrieve(requester, grade) }
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
