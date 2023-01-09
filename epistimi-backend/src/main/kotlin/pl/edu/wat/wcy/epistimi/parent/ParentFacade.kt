package pl.edu.wat.wcy.epistimi.parent

import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.parent.domain.service.ParentAggregatorService
import pl.edu.wat.wcy.epistimi.parent.domain.service.ParentRegistrationService
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest

class ParentFacade(
    private val parentAggregatorService: ParentAggregatorService,
    private val parentRegistrationService: ParentRegistrationService,
) {
    fun getByUserId(userId: UserId): Parent {
        return parentAggregatorService.getByUserId(userId)
    }

    fun registerParents(
        contextOrganization: Organization,
        userRegisterRequests: List<UserRegisterRequest>,
    ): List<ParentRegistrationService.NewParent> {
        return parentRegistrationService.registerParents(contextOrganization, userRegisterRequests)
    }
}
