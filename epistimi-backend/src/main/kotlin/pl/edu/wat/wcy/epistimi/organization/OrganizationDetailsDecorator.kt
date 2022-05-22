package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.user.port.UserRepository

class OrganizationDetailsDecorator(
    private val userRepository: UserRepository,
) {
    fun decorate(organization: Organization) = with(organization) {
        OrganizationDetails(
            id = organization.id,
            name = organization.name,
            admin = userRepository.findById(adminId),
            status = organization.status,
            address = address,
            location = location,
        )
    }
}
