package pl.edu.wat.wcy.epistimi.grade.domain.access

import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.security.ResourceAccessValidator
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class GradeCategoryAccessValidator : ResourceAccessValidator<GradeCategory> {
    override fun canRetrieve(requester: User, resource: GradeCategory): Boolean {
        return isOrganizationAdmin(requester, resource.subject.course.organization) ||
            isUserSubjectTeacher(requester, resource.subject)
    }

    private fun isOrganizationAdmin(requester: User, organization: Organization): Boolean {
        return requester hasRole UserRole.ORGANIZATION_ADMIN &&
            requester.organization!!.id == organization.id
    }

    private fun isUserSubjectTeacher(requester: User, subject: Subject): Boolean {
        return requester.id == subject.teacher.user.id
    }

    override fun canCreate(requester: User, resource: GradeCategory): Boolean {
        return isUserSubjectTeacher(requester, resource.subject)
    }
}
