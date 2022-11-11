package pl.edu.wat.wcy.epistimi.subject.domain.service

import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectNotFoundException
import pl.edu.wat.wcy.epistimi.subject.domain.access.SubjectAccessValidator
import pl.edu.wat.wcy.epistimi.subject.domain.port.SubjectRepository
import pl.edu.wat.wcy.epistimi.user.User

class SubjectAggregatorService(
    private val subjectRepository: SubjectRepository,
    private val subjectAccessValidator: SubjectAccessValidator,
) {
    fun getSubject(
        contextUser: User,
        subjectId: SubjectId,
    ): Subject {
        return subjectRepository.findById(subjectId)
            .also { subject ->
                if (!subjectAccessValidator.canRetrieve(contextUser, subject)) {
                    throw SubjectNotFoundException(subject.id!!)
                }
            }
    }
}
