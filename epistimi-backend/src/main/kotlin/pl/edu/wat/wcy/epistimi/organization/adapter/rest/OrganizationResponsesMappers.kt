package pl.edu.wat.wcy.epistimi.organization.adapter.rest

import pl.edu.wat.wcy.epistimi.organization.OrganizationDetails
import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.user.adapter.rest.toUserResponse

object OrganizationResponseMapper : FromDomainMapper<OrganizationDetails, OrganizationResponse> {
    override fun fromDomain(domainObject: OrganizationDetails) = domainObject.toOrganizationResponse()
}

private fun OrganizationDetails.toOrganizationResponse() = OrganizationResponse(
    id = id!!,
    name = name,
    admin = admin.toUserResponse(),
    status = status.toString(),
    director = director.toUserResponse(),
    address = address,
    location = location,
)

object OrganizationsResponseMapper : FromDomainMapper<List<OrganizationDetails>, OrganizationsResponse> {
    override fun fromDomain(domainObject: List<OrganizationDetails>) =
        OrganizationsResponse(organizations = domainObject.map { it.toOrganizationResponse() })
}
