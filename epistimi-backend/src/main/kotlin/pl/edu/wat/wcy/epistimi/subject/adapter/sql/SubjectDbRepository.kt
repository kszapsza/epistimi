package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.subject.Subject
import pl.edu.wat.wcy.epistimi.subject.SubjectId
import pl.edu.wat.wcy.epistimi.subject.SubjectNotFoundException
import pl.edu.wat.wcy.epistimi.subject.port.SubjectRepository

@Repository
class SubjectDbRepository(
    private val subjectJpaRepository: SubjectJpaRepository,
) : SubjectRepository {

    override fun findById(subjectId: SubjectId): Subject {
        return subjectJpaRepository.findById(subjectId)
            .orElseThrow { SubjectNotFoundException(subjectId) }
    }

    override fun save(subject: Subject): Subject {
        return subjectJpaRepository.save(subject)
    }
}
