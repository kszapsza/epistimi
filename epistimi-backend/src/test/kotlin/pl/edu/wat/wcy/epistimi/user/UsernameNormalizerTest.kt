package pl.edu.wat.wcy.epistimi.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class UsernameNormalizerTest : ShouldSpec({

    should("leave the username unchanged if there are no special characters") {
        // given
        val username = "jan.kowalski"

        // when
        val normalizedUsername = UsernameNormalizer.normalize(username)

        // then
        normalizedUsername shouldBe "jan.kowalski"
    }

    should("map Polish letters into equivalent ASCII letters") {
        // given
        val username = "zażółć.gęśląjaźń"

        // when
        val normalizedUsername = UsernameNormalizer.normalize(username)

        // then
        normalizedUsername shouldBe "zazolc.geslajazn"
    }

    should("map letters with accents into equivalent ASCII letters") {
        // given
        val username = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű"

        // when
        val normalizedUsername = UsernameNormalizer.normalize(username)

        // then
        normalizedUsername shouldBe "AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu"
    }

})