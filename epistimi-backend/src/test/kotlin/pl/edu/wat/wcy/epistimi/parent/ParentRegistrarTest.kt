package pl.edu.wat.wcy.epistimi.parent

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

internal class ParentRegistrarTest : ShouldSpec({

    val parentRepository = mockk<ParentRepository>()
    val userRegistrar = mockk<UserRegistrar>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val parentRegistrar = ParentRegistrar(parentRepository, userRegistrar, organizationContextProvider)

    val organizationAdminUser = User(
        id = UserId("admin_user_id"),
        firstName = "Jan",
        lastName = "Kowalski",
        role = ORGANIZATION_ADMIN,
        username = "j.kowalski",
        passwordHash = "123",
        sex = MALE,
    )

    val organizationStub = Organization(
        id = OrganizationId("organization_id"),
        name = "SP7",
        admin = organizationAdminUser,
        status = ENABLED,
        director = organizationAdminUser,
        address = TestData.address,
        location = null,
    )

    should("register parent along with underlying user profile") {
        // given
        every { organizationContextProvider.provide(UserId("organization_admin_user_id")) } returns organizationStub
        every { userRegistrar.registerUser(ofType(UserRegisterRequest::class)) } answers {
            with(firstArg<UserRegisterRequest>()) {
                NewUser(
                    user = User(
                        id = UserId("user_id"),
                        firstName = firstName,
                        lastName = lastName,
                        role = PARENT,
                        username = "$firstName.$lastName".lowercase(),
                        passwordHash = "654321",
                    ),
                    password = "123456",
                )
            }
        }
        every { parentRepository.save(ofType(Parent::class)) } answers {
            (firstArg<Parent>()).copy(id = ParentId("parent_id"))
        }

        // and
        val userRegisterRequest = UserRegisterRequest(
            firstName = "Jan",
            lastName = "Kowalski",
            role = User.Role.PARENT,
            username = null,
            password = null,
        )

        // when
        val (parent, password) = parentRegistrar.registerParent(
            requesterUserId = UserId("organization_admin_user_id"),
            userRegisterRequest = userRegisterRequest,
        )

        // then
        parent shouldBe Parent(
            id = ParentId("parent_id"),
            user = User(
                id = UserId("user_id"),
                firstName = "Jan",
                lastName = "Kowalski",
                role = PARENT,
                username = "jan.kowalski",
                passwordHash = "654321",
            ),
            organization = organizationStub,
        )
        password shouldBe "123456"

        // and
        verify { organizationContextProvider.provide(UserId("organization_admin_user_id")) }
        verify { userRegistrar.registerUser(userRegisterRequest) }
        verify {
            parentRepository.save(Parent(
                id = null,
                user = User(
                    id = UserId("user_id"),
                    firstName = "Jan",
                    lastName = "Kowalski",
                    role = PARENT,
                    username = "jan.kowalski",
                    passwordHash = "654321",
                ),
                organization = organizationStub,
            ))
        }
    }

})
