package pl.edu.wat.wcy.epistimi.subject.domain.port

import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.UserId

interface SubjectRepository {
    fun findById(subjectId: SubjectId): Subject
    fun findAllByOrganizationId(organizationId: OrganizationId): List<Subject>
    fun findAllByTeacherUserId(teacherUserId: UserId): List<Subject>
    fun findAllByStudentUserId(studentUserId: UserId): List<Subject>
    fun findAllByParentUserId(parentUserId: UserId): List<Subject>
    fun save(subject: Subject): Subject
}
