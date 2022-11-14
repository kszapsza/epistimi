package pl.edu.wat.wcy.epistimi.student.domain.access

import pl.edu.wat.wcy.epistimi.security.ResourceAccessValidator
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class StudentAccessValidator : ResourceAccessValidator<Student> {
    override fun canRetrieve(requester: User, resource: Student): Boolean {
        return requester.id == resource.user.id!! ||
                isUserOneOfStudentParents(requester, resource) ||
                isUserTeacherOrOrganizationAdmin(requester, resource)
    }

    private fun isUserOneOfStudentParents(requester: User, resource: Student): Boolean {
        return requester hasRole UserRole.PARENT &&
                requester.id in resource.parents.map { it.user.id }
    }

    private fun isUserTeacherOrOrganizationAdmin(requester: User, resource: Student): Boolean {
        return requester hasAnyRole setOf(UserRole.ORGANIZATION_ADMIN, UserRole.TEACHER) &&
                requester.organization!!.id == resource.user.organization!!.id
    }
}
