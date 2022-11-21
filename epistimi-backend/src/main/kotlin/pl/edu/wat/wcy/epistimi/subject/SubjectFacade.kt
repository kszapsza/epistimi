package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectRegisterRequest
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectsBySchoolYearAndCourse
import pl.edu.wat.wcy.epistimi.subject.domain.service.SubjectAggregatorService
import pl.edu.wat.wcy.epistimi.subject.domain.service.SubjectRegisterService
import pl.edu.wat.wcy.epistimi.user.domain.User

class SubjectFacade(
    private val subjectAggregatorService: SubjectAggregatorService,
    private val subjectRegisterService: SubjectRegisterService,
) {
    fun getSubject(
        contextUser: User,
        subjectId: SubjectId,
    ): Subject {
        return subjectAggregatorService.getSubject(contextUser, subjectId)
    }

    fun getSubjects(contextUser: User): SubjectsBySchoolYearAndCourse {
        return subjectAggregatorService.getSubjects(contextUser)
    }

    fun registerSubject(
        contextUser: User,
        subjectRegisterRequest: SubjectRegisterRequest,
    ): Subject {
        return subjectRegisterService.registerSubject(contextUser, subjectRegisterRequest)
    }
}
