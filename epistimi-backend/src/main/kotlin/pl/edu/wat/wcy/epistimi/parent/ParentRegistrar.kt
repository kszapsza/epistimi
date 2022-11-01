package pl.edu.wat.wcy.epistimi.parent

import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRole

class ParentRegistrar(
    private val parentRepository: ParentRepository,
    private val userRegistrar: UserRegistrar,
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
    ): List<UserRegistrar.NewUser> {
        return usersData
            .map { it.copy(role = UserRole.PARENT) }
            .let { userRegistrar.registerUsers(contextOrganization, requests = it) }
    }

    private fun registerParents(
        parentUsers: List<UserRegistrar.NewUser>,
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
