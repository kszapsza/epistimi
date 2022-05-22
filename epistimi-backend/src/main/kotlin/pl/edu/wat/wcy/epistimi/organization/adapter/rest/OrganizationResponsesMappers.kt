package pl.edu.wat.wcy.epistimi.organization.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.organization.OrganizationDetails
import pl.edu.wat.wcy.epistimi.organization.OrganizationRegistrar.NewOrganization
import pl.edu.wat.wcy.epistimi.organization.adapter.rest.OrganizationRegisterResponse.NewUserResponse
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponseMapper

object OrganizationResponseMapper : FromDomainMapper<OrganizationDetails, OrganizationResponse> {
    override fun fromDomain(domainObject: OrganizationDetails) = domainObject.toOrganizationResponse()
}

private fun OrganizationDetails.toOrganizationResponse() = OrganizationResponse(
    id = id!!,
    name = name,
    admin = UserResponseMapper.fromDomain(admin),
    status = status.toString(),
    address = address,
    location = location,
)

object OrganizationsResponseMapper : FromDomainMapper<List<OrganizationDetails>, OrganizationsResponse> {
    override fun fromDomain(domainObject: List<OrganizationDetails>) =
        OrganizationsResponse(organizations = domainObject.map { it.toOrganizationResponse() })
}

object OrganizationRegisterResponseMapper : FromDomainMapper<NewOrganization, OrganizationRegisterResponse> {
    override fun fromDomain(domainObject: NewOrganization) = with(domainObject) {
        OrganizationRegisterResponse(
            admin = NewUserResponse(
                user = UserResponseMapper.fromDomain(admin.user),
                password = admin.password,
            ),
            id = organization.id!!,
            name = organization.name,
            status = organization.status.toString(),
            address = organization.address,
            location = organization.location,
        )
    }
}
