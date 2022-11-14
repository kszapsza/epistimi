package pl.edu.wat.wcy.epistimi.user

import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.service.UserAggregatorService
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService

class UserFacade(
    private val userAggregatorService: UserAggregatorService,
    private val userRegistrationService: UserRegistrationService,
) {
    fun getUsers(userRoles: List<UserRole>?): List<User> {
        return userAggregatorService.getUsers(userRoles)
    }

    fun getUserById(userId: UserId): User {
        return userAggregatorService.getUserById(userId)
    }

    /**
     * Registers new user account in Epistimi system.
     *
     * @param contextOrganization admin's organization.
     *  `null` for `EPISTIMI_ADMIN` registering new `ORGANIZATION_ADMIN` user account.
     * @param request Newly registered user data.
     * @return Newly created user with randomly generated credentials.
     */
    fun registerUser(
        contextOrganization: Organization?,
        request: UserRegisterRequest,
    ): UserRegistrationService.NewUser {
        return userRegistrationService.registerUser(contextOrganization, request)
    }

    /**
     * Registers multiple user accounts in Epistimi system.
     *
     * @param contextOrganization admin's organization. New users will be connected with this organization.
     * @param requests Newly registered users' data.
     * @return Newly created users with randomly generated credentials.
     */
    fun registerUsers(
        contextOrganization: Organization,
        requests: List<UserRegisterRequest>,
    ): List<UserRegistrationService.NewUser> {
        return userRegistrationService.registerUsers(contextOrganization, requests)
    }
}
