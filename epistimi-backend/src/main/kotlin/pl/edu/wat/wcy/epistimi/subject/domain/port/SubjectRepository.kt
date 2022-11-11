package pl.edu.wat.wcy.epistimi.subject.domain.port

import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId

interface SubjectRepository {
    fun findById(subjectId: SubjectId): Subject
    fun save(subject: Subject): Subject
}
