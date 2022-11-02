package pl.edu.wat.wcy.epistimi.subject.port

import pl.edu.wat.wcy.epistimi.subject.Subject
import pl.edu.wat.wcy.epistimi.subject.SubjectId

interface SubjectRepository {
    fun findById(subjectId: SubjectId): Subject
    fun save(subject: Subject): Subject
}
