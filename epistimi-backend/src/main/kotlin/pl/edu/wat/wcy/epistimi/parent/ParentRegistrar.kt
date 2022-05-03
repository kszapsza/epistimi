package pl.edu.wat.wcy.epistimi.parent

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
        val parent: Parent,
        val password: String,
    )

    @Transactional
    fun registerParent(
        requesterUserId: UserId,
        userRegisterRequest: UserRegisterRequest,
    ): NewParent {
        val organization = organizationContextProvider.provide(requesterUserId)
            ?: throw ParentBadRequestException("User not managing any organization")
        val parentUser = registerParentUser(userData = userRegisterRequest)

        val newParent = parentRepository.save(
            Parent(
                id = null,
                user = parentUser.user,
                organization = organization,
            )
        )

        return NewParent(
            parent = newParent,
            password = parentUser.password,
        )
    }

    private fun registerParentUser(userData: UserRegisterRequest): UserRegistrar.NewUser {
        return userRegistrar.registerUser(
            userData.copy(role = User.Role.PARENT)
        )
    }

}
