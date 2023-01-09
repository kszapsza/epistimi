package pl.edu.wat.wcy.epistimi.security

import pl.edu.wat.wcy.epistimi.user.domain.User

interface ResourceAccessValidator<TResource> {
    fun canRetrieve(requester: User, resource: TResource): Boolean = true
    fun canCreate(requester: User, resource: TResource): Boolean = true
    fun canModify(requester: User, resource: TResource): Boolean = true
    fun canDelete(requester: User, resource: TResource): Boolean = true
}
