package pl.edu.wat.wcy.epistimi.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT

internal class UserCredentialsGeneratorTest : ShouldSpec({

    val userRepository = mockk<UserRepository>()
    val userCredentialsGenerator = UserCredentialsGenerator(userRepository)

    val userStub = User(
        id = UserId("1"),
        firstName = "Jan",
        lastName = "Kowalski",
        role = STUDENT,
        username = "jan.kowalski",
        passwordHash = "123",
    )

    should("generate a username without number suffix") {
        // given
        every { userRepository.findByUsernameStartingWith("jan.kowalski") } returns emptyList()

        // when
        val generatedUsername = userCredentialsGenerator.generateUsername("Jan", "Kowalski")

        // then
        generatedUsername shouldBe "jan.kowalski"

        // and
        verify {
            userRepository.findByUsernameStartingWith("jan.kowalski")
        }
    }

    should("generate a username with number suffix if needed") {
        // given
        every { userRepository.findByUsernameStartingWith("jan.kowalski") } returns listOf(
            userStub.copy(username = "jan.kowalski"),
            userStub.copy(username = "jan.kowalski.1"),
            userStub.copy(username = "jan.kowalski.2"),
        )

        // when
        val generatedUsername = userCredentialsGenerator.generateUsername("Jan", "Kowalski")

        // then
        generatedUsername shouldBe "jan.kowalski.3"

        // and
        verify {
            userRepository.findByUsernameStartingWith("jan.kowalski")
        }
    }

})
