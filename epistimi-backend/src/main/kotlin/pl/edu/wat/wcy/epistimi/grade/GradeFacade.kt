package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeIssueRequest
import pl.edu.wat.wcy.epistimi.grade.domain.GradesWithStatistics
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeIssuingService
import pl.edu.wat.wcy.epistimi.user.domain.User

class GradeFacade(
    private val gradeAggregatorService: GradeAggregatorService,
    private val gradeIssuingService: GradeIssuingService,
) {
    fun getGradeById(
        requester: User,
        gradeId: GradeId,
    ): Grade {
        return gradeAggregatorService.getGradeById(requester, gradeId)
    }

    fun getGrades(
        requester: User,
        filters: GradeFilters,
    ): GradesWithStatistics {
        return gradeAggregatorService.getGrades(requester, filters)
    }

    fun issueGrade(
        requester: User,
        gradeIssueRequest: GradeIssueRequest,
    ): Grade {
        return gradeIssuingService.issueGrade(requester, gradeIssueRequest)
    }
}
