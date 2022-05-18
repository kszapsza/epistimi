package pl.edu.wat.wcy.epistimi.user

import org.passay.AllowedCharacterRule
import org.passay.CharacterData
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.PasswordGenerator
import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.user.port.UserRepository
import java.util.Locale

data class Credentials(
    val username: String,
    val password: String,
)

@Component
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
        val baseGeneratedUsername = "${firstName}.${lastName}".lowercase(Locale.getDefault())
        val usernamesStartingWithBase = userRepository.findByUsernameStartingWith(baseGeneratedUsername)

        // TODO: remove non-ASCII characters (e.g. polish ogonki)

        return if (usernamesStartingWithBase.isEmpty()) {
            baseGeneratedUsername
        } else {
            "${baseGeneratedUsername}.${usernamesStartingWithBase.size}"
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
