package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectRegisterRequest
import pl.edu.wat.wcy.epistimi.subject.domain.service.SubjectAggregatorService
import pl.edu.wat.wcy.epistimi.subject.domain.service.SubjectRegisterService
import pl.edu.wat.wcy.epistimi.user.User

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

    fun registerSubject(
        contextOrganization: Organization,
        subjectRegisterRequest: SubjectRegisterRequest,
    ): Subject {
        return subjectRegisterService.registerSubject(contextOrganization, subjectRegisterRequest)
    }
}
