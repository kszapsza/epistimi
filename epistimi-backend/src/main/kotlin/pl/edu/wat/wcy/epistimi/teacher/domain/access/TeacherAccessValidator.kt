package pl.edu.wat.wcy.epistimi.teacher.domain.access

import pl.edu.wat.wcy.epistimi.security.ResourceAccessValidator
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class TeacherAccessValidator : ResourceAccessValidator<Teacher> {
    override fun canRetrieve(requester: User, resource: Teacher): Boolean {
        return requester.id == resource.user.id
                || isUserAnOrganizationAdmin(requester, resource)
    }

    private fun isUserAnOrganizationAdmin(requester: User, resource: Teacher): Boolean {
        return requester hasRole UserRole.ORGANIZATION_ADMIN &&
                requester.organization!!.id == resource.user.organization!!.id
    }
}
