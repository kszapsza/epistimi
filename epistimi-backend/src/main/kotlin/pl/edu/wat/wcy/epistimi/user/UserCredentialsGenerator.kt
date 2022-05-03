package pl.edu.wat.wcy.epistimi.user

import org.passay.AllowedCharacterRule
import org.passay.CharacterData
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.PasswordGenerator
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class UserCredentialsGenerator(
    private val userRepository: UserRepository,
) {
    fun generateUsername(firstName: String, lastName: String): String {
        val baseGeneratedUsername = "${firstName}.${lastName}".lowercase(Locale.getDefault())
        val usernamesStartingWithBase = userRepository.findByUsernameStartingWith(baseGeneratedUsername)

        return if (usernamesStartingWithBase.isEmpty()) {
            baseGeneratedUsername
        } else {
            "${baseGeneratedUsername}.${usernamesStartingWithBase.size}"
        }
    }

    fun generatePassword(): String {
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
