package pl.edu.wat.wcy.epistimi.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

internal class UserRegistrarTest : ShouldSpec({

    val userRepository = mockk<UserRepository>()
    val credentialsGenerator = mockk<UserCredentialsGenerator>()
    val passwordEncoder = mockk<PasswordEncoder>()

    val userRegistrar = UserRegistrar(userRepository, credentialsGenerator, passwordEncoder)

    should("successfully register user with provided credentials") {
        // given
        every { userRepository.save(ofType(User::class)) } answers { firstArg<User>().copy(id = UserId("user_id")) }
        every { passwordEncoder.encode(ofType(CharSequence::class)) } returnsArgument 0

        // when
        val registeredUser = userRegistrar.registerUser(
            UserRegisterRequest(
                firstName = "Jan",
                lastName = "Kowalski",
                role = STUDENT,
                username = "login",
                password = "pwd123",
            )
        )

        // then
        registeredUser.user.username shouldBe "login"
        registeredUser.password shouldBe "pwd123"
    }

    should("successfully register user with generated credentials if not provided") {
        // given
        every { userRepository.save(ofType(User::class)) } answers { firstArg<User>().copy(id = UserId("user_id")) }
        every { passwordEncoder.encode(ofType(CharSequence::class)) } returnsArgument 0
        every { credentialsGenerator.generate("Jan", "Kowalski") } returns
            Credentials("jan.kowalski", "123")

        // when
        val registeredUser = userRegistrar.registerUser(
            UserRegisterRequest(
                firstName = "Jan",
                lastName = "Kowalski",
                role = STUDENT,
                username = null,
                password = null,
            )
        )

        // then
        registeredUser.user.username shouldBe "jan.kowalski"
        registeredUser.password shouldBe "123"

        // and
        verify { credentialsGenerator.generate("Jan", "Kowalski") }
    }
})
