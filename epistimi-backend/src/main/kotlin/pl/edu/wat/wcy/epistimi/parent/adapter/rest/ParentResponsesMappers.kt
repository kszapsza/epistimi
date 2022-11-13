package pl.edu.wat.wcy.epistimi.parent.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponseMapper

object ParentResponseMapper : FromDomainMapper<Parent, ParentResponse> {
    override fun fromDomain(domainObject: Parent) = domainObject.toParentResponse()
}

private fun Parent.toParentResponse() = ParentResponse(
    id = id,
    user = UserResponseMapper.fromDomain(user),
)
