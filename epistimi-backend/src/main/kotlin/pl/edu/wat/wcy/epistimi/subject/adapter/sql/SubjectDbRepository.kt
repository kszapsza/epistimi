package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectNotFoundException
import pl.edu.wat.wcy.epistimi.subject.domain.port.SubjectRepository

@Repository
class SubjectDbRepository(
    private val subjectJpaRepository: SubjectJpaRepository,
) : SubjectRepository {

    override fun findById(subjectId: SubjectId): Subject {
        return subjectJpaRepository.findById(subjectId.value)
            .orElseThrow { SubjectNotFoundException(subjectId) }
    }

    override fun save(subject: Subject): Subject {
        return subjectJpaRepository.save(subject)
    }
}
