package pl.edu.wat.wcy.epistimi.subject.domain.access

import pl.edu.wat.wcy.epistimi.security.ResourceAccessValidator
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserRole

class SubjectAccessValidator : ResourceAccessValidator<Subject> {
    override fun canRetrieve(requester: User, resource: Subject): Boolean {
        return isRequesterClassTeacher(requester, resource) ||
            isRequesterOneOfSubjectStudents(requester, resource) ||
            isRequesterOneOfSubjectStudentsParents(requester, resource) ||
            isRequesterAdminOfSubjectOrganization(requester, resource)
    }

    private fun isRequesterClassTeacher(requester: User, subject: Subject): Boolean {
        return requester.id == subject.teacher.user.id
    }

    private fun isRequesterAdminOfSubjectOrganization(requester: User, subject: Subject): Boolean {
        return requester hasRole UserRole.ORGANIZATION_ADMIN &&
            requester.organization!!.id == subject.course.organization.id
    }

    private fun isRequesterOneOfSubjectStudents(requester: User, subject: Subject): Boolean {
        val studentUserIds = subject.course.subjects
            .flatMap { it.course.students }
            .map { it.user.id }
        return requester.id in studentUserIds
    }

    private fun isRequesterOneOfSubjectStudentsParents(requester: User, subject: Subject): Boolean {
        val parentUserIds = subject.course.subjects
            .flatMap { it.course.students }
            .flatMap { it.parents }
            .map { it.user.id }
        return requester.id in parentUserIds
    }
}
