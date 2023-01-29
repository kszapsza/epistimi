package pl.edu.wat.wcy.epistimi.parent.domain.service

import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.parent.domain.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.UserFacade
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService

class ParentRegistrationService(
    private val parentRepository: ParentRepository,
    private val userFacade: UserFacade,
) {
    data class NewParent(
        val parent: Parent,
        val password: String,
    )

    fun registerParents(
        contextOrganization: Organization,
        userRegisterRequests: List<UserRegisterRequest>,
    ): List<NewParent> {
        val parentUsers = registerParentUsers(contextOrganization, userRegisterRequests)
        val parents = registerParents(parentUsers)

        return parentUsers.zip(parents)
            .map { (parentUser, parent) ->
                NewParent(
                    parent = Parent(
                        id = parent.id,
                        user = parentUser.user,
                    ),
                    password = parentUser.password,
                )
            }
    }

    private fun registerParentUsers(
        contextOrganization: Organization,
        usersData: List<UserRegisterRequest>,
    ): List<UserRegistrationService.NewUser> {
        return usersData
            .map { it.copy(role = UserRole.PARENT) }
            .let { userFacade.registerUsers(contextOrganization, requests = it) }
    }

    private fun registerParents(
        parentUsers: List<UserRegistrationService.NewUser>,
    ): List<Parent> {
        return parentRepository.saveAll(
            parentUsers.map { (user) ->
                Parent(
                    id = null,
                    user = user,
                )
            }
        )
    }
}
