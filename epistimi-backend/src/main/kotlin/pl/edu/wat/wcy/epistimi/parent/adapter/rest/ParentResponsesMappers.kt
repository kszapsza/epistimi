package pl.edu.wat.wcy.epistimi.parent.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.parent.ParentDetails
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponseMapper

object ParentResponseMapper : FromDomainMapper<ParentDetails, ParentResponse> {
    override fun fromDomain(domainObject: ParentDetails) = domainObject.toParentResponse()
}

private fun ParentDetails.toParentResponse() = ParentResponse(
    id = id,
    user = UserResponseMapper.fromDomain(user),
)
