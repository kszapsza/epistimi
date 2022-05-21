package pl.edu.wat.wcy.epistimi.student

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.User.Sex.OTHER
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

internal class StudentRegisterRequestValidationTest : ShouldSpec({

    val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    val validator: Validator = validatorFactory.validator

    afterSpec {
        validatorFactory.close()
    }

    val validUserRegisterRequest = UserRegisterRequest(
        firstName = "Jan",
        lastName = "Kowalski",
        role = STUDENT,
        address = TestData.address,
    )

    forAll(
        row(
            listOf(
                validUserRegisterRequest.copy(role = PARENT, firstName = "Aneta", sex = FEMALE),
            ),
            "has one parent",
        ),
        row(
            listOf(
                validUserRegisterRequest.copy(role = PARENT, firstName = "Aneta", sex = FEMALE),
                validUserRegisterRequest.copy(role = PARENT, firstName = "Jerzy", sex = MALE),
            ),
            "has two parents",
        ),
    ) { parentUserRequests, scenario ->
        should("have no violations if list of parents $scenario") {
            // given
            val registerRequest = StudentRegisterRequest(
                courseId = CourseId("course_id"),
                user = validUserRegisterRequest.copy(role = STUDENT),
                parents = parentUserRequests
            )

            // when
            val violations = validator.validate(registerRequest)

            // then
            violations shouldHaveSize 0
        }
    }

    forAll(
        row(
            emptyList(),
            "is an empty list",
        ),
        row(
            listOf(
                validUserRegisterRequest.copy(role = PARENT, firstName = "Aneta", sex = FEMALE),
                validUserRegisterRequest.copy(role = PARENT, firstName = "Jerzy", sex = MALE),
                validUserRegisterRequest.copy(role = PARENT, firstName = "Ktoś", sex = OTHER),
            ),
            "has three parents",
        ),
    ) { parentUserRequests, scenario ->
        should("have a violation if list of parents $scenario") {
            // given
            val registerRequest = StudentRegisterRequest(
                courseId = CourseId("course_id"),
                user = validUserRegisterRequest.copy(role = STUDENT),
                parents = parentUserRequests
            )

            // when
            val violations = validator.validate(registerRequest)

            // then
            violations shouldHaveSize 1

            with(violations.iterator().next()) {
                message shouldBe "At least 1 and at most 2 parents required"
            }
        }
    }
})
