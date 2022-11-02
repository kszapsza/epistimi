package pl.edu.wat.wcy.epistimi.user.credentials

import org.passay.AllowedCharacterRule
import org.passay.CharacterData
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.PasswordGenerator
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

data class Credentials(
    val username: String,
    val password: String,
)

class UserCredentialsGenerator(
    private val userRepository: UserRepository,
) {
    fun generate(firstName: String, lastName: String): Credentials {
        return Credentials(
            username = generateUsername(firstName, lastName),
            password = generatePassword(),
        )
    }

    private fun generateUsername(firstName: String, lastName: String): String {
        val baseGeneratedUsername = UsernameNormalizer.normalize("$firstName.$lastName")
        val usernamesStartingWithBase = userRepository.findByUsernameStartingWith(baseGeneratedUsername)

        return if (usernamesStartingWithBase.isEmpty()) {
            baseGeneratedUsername
        } else {
            "$baseGeneratedUsername.${usernamesStartingWithBase.size}"
        }
    }

    private fun generatePassword(): String {
        val specialChars = object : CharacterData {
            override fun getErrorCode() = AllowedCharacterRule.ERROR_CODE
            override fun getCharacters() = "!@#$%^&*()_+"
        }
        return PasswordGenerator().generatePassword(
            10,
            CharacterRule(specialChars).apply { numberOfCharacters = 2 },
            CharacterRule(EnglishCharacterData.LowerCase).apply { numberOfCharacters = 2 },
            CharacterRule(EnglishCharacterData.UpperCase).apply { numberOfCharacters = 2 },
            CharacterRule(EnglishCharacterData.Digit).apply { numberOfCharacters = 2 }
        )
    }
}
