package pl.edu.wat.wcy.epistimi.user

import org.springframework.security.crypto.password.PasswordEncoder
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

class UserRegistrar(
    private val userRepository: UserRepository,
    private val credentialsGenerator: UserCredentialsGenerator,
    private val passwordEncoder: PasswordEncoder,
) {
    data class NewUser(
        val user: User,
        val password: String,
    )

    /**
     * Registers new user account in Epistimi system.
     *
     * @param contextOrganization Admin's organization.
     *  `null` for `EPISTIMI_ADMIN` registering new `ORGANIZATION_ADMIN` user account.
     * @param request Newly registered user data.
     * @return Newly created user with randomly generated credentials.
     */
    fun registerUser(
        contextOrganization: Organization?,
        request: UserRegisterRequest,
    ): NewUser {
        val credentials = getCredentials(request)
        val user = userRepository.save(user = request.toUser(credentials, organization = contextOrganization))

        return NewUser(user = user, password = credentials.password)
    }

    private fun getCredentials(request: UserRegisterRequest): Credentials {
        return if (request.username == null || request.password == null) {
            credentialsGenerator.generate(request.firstName, request.lastName)
        } else {
            Credentials(
                username = request.username,
                password = request.password,
            )
        }
    }

    private fun UserRegisterRequest.toUser(credentials: Credentials, organization: Organization?): User {
        return User(
            id = null,
            organization = organization,
            firstName = firstName,
            lastName = lastName,
            role = role,
            username = credentials.username,
            passwordHash = passwordEncoder.encode(credentials.password),
            pesel = pesel,
            sex = sex,
            email = email,
            phoneNumber = phoneNumber,
            street = address?.street,
            postalCode = address?.postalCode,
            city = address?.city,
        )
    }

    /**
     * Registers multiple user accounts in Epistimi system.
     *
     * @param contextOrganization Admin's organization. New users will be connected with this organization.
     * @param requests Newly registered users' data.
     * @return Newly created users with randomly generated credentials.
     */
    fun registerUsers(
        contextOrganization: Organization,
        requests: List<UserRegisterRequest>,
    ): List<NewUser> {
        val credentials: List<Credentials> = requests
            .map { credentialsGenerator.generate(it.firstName, it.lastName) }
        val usersToRegister: List<User> = requests
            .zip(credentials)
            .map { (request, credentials) -> request.toUser(credentials, contextOrganization) }
        return userRepository.saveAll(usersToRegister)
            .mapIndexed { index, user ->
                NewUser(
                    user = user,
                    password = credentials[index].password,
                )
            }
    }
}
