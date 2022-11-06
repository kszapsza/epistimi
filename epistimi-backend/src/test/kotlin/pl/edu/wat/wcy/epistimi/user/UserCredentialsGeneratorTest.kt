package pl.edu.wat.wcy.epistimi.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldContainADigit
import io.kotest.matchers.string.shouldHaveLength
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

internal class UserCredentialsGeneratorTest : ShouldSpec({

    val userRepository = mockk<UserRepository>()
    val userCredentialsGenerator = UserCredentialsGenerator(userRepository)

    should("generate a username without number suffix") {
        // given
        every { userRepository.findByUsernameStartingWith("jan.kowalski") } returns emptyList()

        // when
        val (generatedUsername) = userCredentialsGenerator.generate("Jan", "Kowalski")

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
            TestData.Users.student.copy(username = "jan.kowalski"),
            TestData.Users.student.copy(username = "jan.kowalski.1"),
            TestData.Users.student.copy(username = "jan.kowalski.2"),
        )

        // when
        val (generatedUsername) = userCredentialsGenerator.generate("Jan", "Kowalski")

        // then
        generatedUsername shouldBe "jan.kowalski.3"

        // and
        verify {
            userRepository.findByUsernameStartingWith("jan.kowalski")
        }
    }

    should("generate random password in accordance with provided rules") {
        // when
        val (_, generatedPassword) = userCredentialsGenerator.generate("Jan", "Kowalski")

        // then
        with(generatedPassword) {
            shouldHaveLength(10)
            shouldContainADigit()
            shouldContain(Regex("[a-z]"))
            shouldContain(Regex("[A-Z]"))
            shouldContain(Regex("[!@#$%^&*()_+]"))
        }
    }
})
