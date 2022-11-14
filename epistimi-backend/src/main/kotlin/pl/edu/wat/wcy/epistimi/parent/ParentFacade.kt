package pl.edu.wat.wcy.epistimi.parent

import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.parent.domain.service.ParentRegistrationService
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest

class ParentFacade(
    private val parentRegistrationService: ParentRegistrationService,
) {
    fun registerParents(
        contextOrganization: Organization,
        userRegisterRequests: List<UserRegisterRequest>,
    ): List<ParentRegistrationService.NewParent> {
        return parentRegistrationService.registerParents(contextOrganization, userRegisterRequests)
    }
}
