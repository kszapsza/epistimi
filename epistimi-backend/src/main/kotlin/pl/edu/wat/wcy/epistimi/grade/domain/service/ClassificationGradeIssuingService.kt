package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGradeIssueForbiddenException
import pl.edu.wat.wcy.epistimi.grade.domain.request.ClassificationGradeIssueRequest
import pl.edu.wat.wcy.epistimi.grade.domain.access.ClassificationGradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.ClassificationGradeRepository
import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.student.StudentFacade
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.user.domain.User

class ClassificationGradeIssuingService(
    private val classificationGradeRepository: ClassificationGradeRepository,
    private val classificationGradeAccessValidator: ClassificationGradeAccessValidator,
    private val subjectFacade: SubjectFacade,
    private val studentFacade: StudentFacade,
    private val teacherFacade: TeacherFacade,
) {
    companion object {
        private val logger by logger()
    }

    fun issueClassificationGrade(
        contextUser: User,
        issueRequest: ClassificationGradeIssueRequest,
    ): ClassificationGrade {
        val classificationGrade = buildClassificationGrade(contextUser, issueRequest)
        if (classificationGradeAccessValidator.canCreate(contextUser, classificationGrade)) {
            return classificationGradeRepository.save(classificationGrade)
        }
        logger.warn(
            "User with id [${contextUser.id!!}] attempted to issue grade " +
                "(subjectId=[${classificationGrade.subject.id!!.value}], studentId=[${classificationGrade.student.id!!.value}]). " +
                "Access was denied."
        )
        throw ClassificationGradeIssueForbiddenException(
            subjectId = issueRequest.subjectId,
            studentId = issueRequest.studentId,
        )
    }

    private fun buildClassificationGrade(
        contextUser: User,
        issueRequest: ClassificationGradeIssueRequest,
    ): ClassificationGrade {
        return ClassificationGrade(
            id = null,
            subject = subjectFacade.getSubject(contextUser, issueRequest.subjectId),
            student = studentFacade.getStudent(contextUser, issueRequest.studentId),
            issuedAt = null,
            updatedAt = null,
            issuedBy = teacherFacade.getTeacherByUserId(contextUser, contextUser.id!!),
            value = issueRequest.value,
            period = issueRequest.period,
            isProposal = issueRequest.isProposal,
        )
    }
}
