package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository

class GradeIssuingService(
    private val gradeRepository: GradeRepository,
) {
    fun issueGrade(): Grade {
        TODO()
    }

    fun batchIssueGrades(): List<Grade> {
        TODO()
    }
}
