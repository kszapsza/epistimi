package pl.edu.wat.wcy.epistimi.parent

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

@Service
class ParentRegistrar(
    private val parentRepository: ParentRepository,
    private val userRegistrar: UserRegistrar,
    private val organizationContextProvider: OrganizationContextProvider,
) {
    data class NewParent(
        val parent: ParentDetails,
        val password: String,
    )

    fun registerParents(
        requesterUserId: UserId,
        userRegisterRequests: List<UserRegisterRequest>,
    ): List<NewParent> {
        val organization = organizationContextProvider.provide(requesterUserId)
            ?: throw ParentBadRequestException("User not managing any organization")

        val parentUsers = registerParentUsers(usersData = userRegisterRequests)
        val parents = registerParents(parentUsers, organization)

        return parentUsers.zip(parents)
            .map { (parentUser, parent) ->
                NewParent(
                    parent = ParentDetails(
                        id = parent.id,
                        user = parentUser.user,
                        organization = organization,
                    ),
                    password = parentUser.password,
                )
            }
    }

    private fun registerParentUsers(
        usersData: List<UserRegisterRequest>,
    ): List<UserRegistrar.NewUser> {
        return usersData
            .map { it.copy(role = User.Role.PARENT) }
            .let { userRegistrar.registerUsers(it) }
    }

    private fun registerParents(
        parentUsers: List<UserRegistrar.NewUser>,
        organization: Organization,
    ): List<Parent> {
        return parentRepository.saveAll(
            parentUsers.map { (user) ->
                Parent(
                    id = null,
                    userId = user.id!!,
                    organizationId = organization.id!!,
                )
            })
    }
}
