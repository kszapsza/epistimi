package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.request.ClassificationGradeIssueRequest
import pl.edu.wat.wcy.epistimi.grade.domain.service.ClassificationGradeIssuingService
import pl.edu.wat.wcy.epistimi.user.domain.User

class ClassificationGradeFacade(
    private val classificationGradeIssuingService: ClassificationGradeIssuingService,
) {
    fun issueClassificationGrade(
        contextUser: User,
        issueRequest: ClassificationGradeIssueRequest,
    ): ClassificationGrade {
        return classificationGradeIssuingService.issueClassificationGrade(contextUser, issueRequest)
    }
}
