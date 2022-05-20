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
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest

internal class ParentRegistrarTest : ShouldSpec({

    val parentRepository = mockk<ParentRepository>()
    val userRegistrar = mockk<UserRegistrar>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val parentRegistrar = ParentRegistrar(
        parentRepository,
        userRegistrar,
        organizationContextProvider
    )

    val organizationStub = Organization(
        id = OrganizationId("organization_id"),
        name = "SP7",
        adminId = UserId("admin_user_id"),
        status = ENABLED,
        directorId = UserId("admin_user_id"),
        address = TestData.address,
        location = null,
    )

    should("register parent along with underlying user profile") {
        // given
        every { organizationContextProvider.provide(UserId("organization_admin_user_id")) } returns organizationStub
        every { userRegistrar.registerUsers(any()) } answers {
            with(firstArg<List<UserRegisterRequest>>()[0]) {
                listOf(
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
                )
            }
        }
        every { parentRepository.saveAll(any()) } answers {
            firstArg<List<Parent>>().map { it.copy(id = ParentId("parent_id")) }
        }

        // and
        val userRegisterRequest = UserRegisterRequest(
            firstName = "Jan",
            lastName = "Kowalski",
            role = PARENT,
        )

        // when
        val (parent, password) = parentRegistrar.registerParents(
            requesterUserId = UserId("organization_admin_user_id"),
            userRegisterRequests = listOf(userRegisterRequest),
        )[0]

        // then
        parent shouldBe ParentDetails(
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
        verify { userRegistrar.registerUsers(listOf(userRegisterRequest)) }
        verify {
            parentRepository.saveAll(
                listOf(
                    Parent(
                        id = null,
                        userId = UserId("user_id"),
                        organizationId = organizationStub.id!!,
                    )
                )
            )
        }
    }
})
