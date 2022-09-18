package pl.edu.wat.wcy.epistimi.parent

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import java.util.UUID

internal class ParentRegistrarTest : ShouldSpec({

    val parentRepository = mockk<ParentRepository>()
    val userRegistrar = mockk<UserRegistrar>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val parentRegistrar = ParentRegistrar(
        parentRepository,
        userRegistrar,
        organizationContextProvider,
    )

    val organizationAdminId = TestData.organization.admin.id!!
    val parentUserId = TestData.Users.parent.id!!
    val parentId = ParentId(UUID.randomUUID())

    fun stubOrganizationContextProvider() {
        every {
            organizationContextProvider.provide(organizationAdminId)
        } returns TestData.organization
    }

    fun stubUserRegistrar() {
        every {
            userRegistrar.registerUsers(any())
        } answers {
            firstArg<List<UserRegisterRequest>>().map { request ->
                NewUser(
                    user = User(
                        id = parentUserId,
                        firstName = request.firstName,
                        lastName = request.lastName,
                        role = request.role,
                        username = "${request.firstName}.${request.lastName}".lowercase(),
                        passwordHash = "654321",
                    ),
                    password = "123456",
                )
            }
        }
    }

    fun stubParentRepository() {
        every {
            parentRepository.saveAll(any())
        } answers {
            firstArg<List<Parent>>().map { it.copy(id = parentId) }
        }
    }

    should("register parent along with underlying user profile") {
        // given
        stubOrganizationContextProvider()
        stubUserRegistrar()
        stubParentRepository()

        // and
        val userRegisterRequest = UserRegisterRequest(
            firstName = "Jan",
            lastName = "Kowalski",
            role = PARENT,
        )

        // when
        val (parent, password) = parentRegistrar.registerParents(
            requesterUserId = organizationAdminId,
            userRegisterRequests = listOf(userRegisterRequest),
        )[0]

        // then
        val parentUser = User(
            id = parentUserId,
            firstName = "Jan",
            lastName = "Kowalski",
            role = PARENT,
            username = "jan.kowalski",
            passwordHash = "654321",
        )

        parent shouldBe Parent(
            id = parentId,
            user = parentUser,
            organization = TestData.organization,
        )
        password shouldBe "123456"

        // and
        verify { organizationContextProvider.provide(organizationAdminId) }
        verify { userRegistrar.registerUsers(listOf(userRegisterRequest)) }
        verify {
            parentRepository.saveAll(
                listOf(
                    Parent(
                        id = null,
                        user = parentUser,
                        organization = TestData.organization,
                    ),
                ),
            )
        }
    }
})
