package pl.edu.wat.wcy.epistimi.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

internal class UserRegisterRequestValidationTest : ShouldSpec({

    val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    val validator: Validator = validatorFactory.validator

    afterSpec {
        validatorFactory.close()
    }

    val validRegisterRequest = UserRegisterRequest(
        firstName = "Jan",
        lastName = "Kowalski",
        role = STUDENT,
        username = "j.kowalski",
        password = "123456",
        pesel = "01211684935",
        sex = MALE,
        email = "j.kowalski@op.pl",
        phoneNumber = "+48514498100",
        address = TestData.address,
    )

    should("have no violations for valid course create request") {
        // when
        val violations = validator.validate(validRegisterRequest)

        // then
        violations.shouldBeEmpty()
    }

    should("have a PESEL violation for invalid PESEL") {
        forAll(
            row(""),
            row(" "),
            row("123"),
            row("01211684934"),
            row("01211684984"),
            row("012116849355"),
        ) { invalidPesel ->
            // given
            val invalidRequest = validRegisterRequest.copy(pesel = invalidPesel)

            // when
            val violations = validator.validate(invalidRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "invalid Polish National Identification Number (PESEL)"
            }
        }
    }

    should("have an e-mail violation for invalid e-mail address") {
        forAll(
            row(" "),
            row(".com"),
            row("gmail.com"),
            row("@gmail.com"),
            row("@gmail"),
            row("example.gmail.com"),
        ) { invalidEmail ->
            // given
            val invalidRequest = validRegisterRequest.copy(email = invalidEmail)

            // when
            val violations = validator.validate(invalidRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "must be a well-formed email address"
            }
        }
    }
})
