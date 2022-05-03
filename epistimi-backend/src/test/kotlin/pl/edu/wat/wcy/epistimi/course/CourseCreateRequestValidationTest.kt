package pl.edu.wat.wcy.epistimi.course

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import pl.edu.wat.wcy.epistimi.course.dto.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import java.time.LocalDate
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

internal class CourseCreateRequestValidationTest : ShouldSpec({
    val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    val validator: Validator = validatorFactory.validator

    afterSpec {
        validatorFactory.close()
    }

    val validCreateRequest = CourseCreateRequest(
        codeNumber = 1,
        codeLetter = "b",
        schoolYearBegin = LocalDate.now().plusMonths(1),
        schoolYearSemesterEnd = LocalDate.now().plusMonths(2),
        schoolYearEnd = LocalDate.now().plusMonths(3),
        classTeacherId = TeacherId("teacher_id"),
        profile = null,
        profession = null,
        specialization = null,
    )

    should("have no violations for valid course create request") {
        // when
        val violations = validator.validate(validCreateRequest)

        // then
        violations.shouldBeEmpty()
    }

    should("have a violation if code number is lower than 1") {
        forAll(
            row(-42),
            row(-1),
            row(0),
        ) { codeNumber ->
            // given
            val createRequest = validCreateRequest.copy(codeNumber = codeNumber)

            // when
            val violations = validator.validate(createRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "Code number should be >= 1"
            }
        }
    }

    should("have a violation if code number is higher than 8") {
        forAll(
            row(9),
            row(10),
            row(100),
        ) { codeNumber ->
            // given
            val createRequest = validCreateRequest.copy(codeNumber = codeNumber)

            // when
            val violations = validator.validate(createRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "Code number should be <= 8"
            }
        }
    }

    should("have a violation if code letter does not match regex") {
        forAll(
            row("123"),
            row("zażółć"),
            row("ab1"),
        ) { codeLetter ->
            // given
            val createRequest = validCreateRequest.copy(codeLetter = codeLetter)

            // when
            val violations = validator.validate(createRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "Only lowercase a-z letters allowed"
            }
        }
    }

    should("have a violation if school year begin date is in past") {
        forAll(
            row(LocalDate.now().minusDays(42)),
            row(LocalDate.now().minusMonths(3)),
            row(LocalDate.now().minusYears(1)),
        ) { date ->
            // given
            val createRequest = validCreateRequest.copy(
                schoolYearBegin = date,
            )

            // when
            val violations = validator.validate(createRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "School year beginning cannot occur in past"
            }
        }
    }

    should("have a violation if semester end date is in past") {
        forAll(
            row(LocalDate.now().minusDays(42)),
            row(LocalDate.now().minusMonths(3)),
            row(LocalDate.now().minusYears(1)),
        ) { date ->
            // given
            val createRequest = validCreateRequest.copy(
                schoolYearSemesterEnd = date,
            )

            // when
            val violations = validator.validate(createRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "School year semester end cannot occur in past"
            }
        }
    }

    should("have a violation if school year end date is in past") {
        forAll(
            row(LocalDate.now().minusDays(42)),
            row(LocalDate.now().minusMonths(3)),
            row(LocalDate.now().minusYears(1)),
        ) { date ->
            // given
            val createRequest = validCreateRequest.copy(
                schoolYearEnd = date,
            )

            // when
            val violations = validator.validate(createRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "School year end cannot occur in past"
            }
        }
    }
})
