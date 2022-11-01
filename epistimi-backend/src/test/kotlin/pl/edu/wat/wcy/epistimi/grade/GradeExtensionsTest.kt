package pl.edu.wat.wcy.epistimi.grade

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.TestUtils
import java.math.BigDecimal
import java.util.UUID

internal class GradeExtensionsTest : ShouldSpec({

    val testGradeCategory = GradeCategory(
        id = GradeCategoryId(UUID.randomUUID()),
        subject = TestData.subject,
        name = "Sprawdzian",
        defaultWeight = 2,
        color = null,
    )

    val testGrade = Grade(
        id = null,
        subject = TestData.subject,
        student = TestData.student,
        issuedBy = TestData.teacher,
        issuedAt = TestUtils.parseDateTime("2012-09-10 14:05"),
        updatedAt = null,
        value = GradeValue.GOOD,
        weight = 1,
        category = testGradeCategory,
        countTowardsAverage = true,
        comment = null,
    )

    should("return null BigDecimal reference for empty grades collection") {
        // given
        val grades = emptyList<Grade>()

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage.shouldBeNull()
    }

    should("calculate weighted average for one element grade collection") {
        // given
        val grades = listOf(
            testGrade.copy(value = GradeValue.SATISFACTORY, weight = 3),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("3.00")
    }

    should("calculate weighted average for multiple element grade collection") {
        // given
        val grades = listOf(
            testGrade.copy(value = GradeValue.EXCELLENT, weight = 13),
            testGrade.copy(value = GradeValue.VERY_GOOD, weight = 8),
            testGrade.copy(value = GradeValue.GOOD, weight = 5),
            testGrade.copy(value = GradeValue.SATISFACTORY, weight = 3),
            testGrade.copy(value = GradeValue.ACCEPTABLE, weight = 2),
            testGrade.copy(value = GradeValue.UNSATISFACTORY, weight = 1),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("4.75")
    }

    should("calculate weighted average ignoring grades marked as not counted towards average") {
        // given
        val grades = listOf(
            testGrade.copy(value = GradeValue.GOOD, weight = 3, countTowardsAverage = false),
            testGrade.copy(value = GradeValue.EXCELLENT, weight = 2),
            testGrade.copy(value = GradeValue.UNSATISFACTORY, weight = 1),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("4.33")
    }

    should("calculate weighted average ignoring grades without numeric values") {
        // given
        val grades = listOf(
            testGrade.copy(value = GradeValue.NO_ASSIGNMENT, weight = 3),
            testGrade.copy(value = GradeValue.UNPREPARED, weight = 3),
            testGrade.copy(value = GradeValue.UNCLASSIFIED, weight = 3),
            testGrade.copy(value = GradeValue.ATTENDED, weight = 3),
            testGrade.copy(value = GradeValue.DID_NOT_ATTEND, weight = 3),
            testGrade.copy(value = GradeValue.PASSED, weight = 3),
            testGrade.copy(value = GradeValue.FAILED, weight = 3),
            testGrade.copy(value = GradeValue.ABSENT, weight = 3),
            testGrade.copy(value = GradeValue.EXEMPT, weight = 3),
            testGrade.copy(value = GradeValue.UNSATISFACTORY, weight = 2),
            testGrade.copy(value = GradeValue.EXCELLENT, weight = 2),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("3.50")
    }

    should("calculate weighted average ignoring \"minuses\" and \"pluses\"") {
        // given
        val grades = listOf(
            testGrade.copy(value = GradeValue.EXCELLENT, weight = 1),
            testGrade.copy(value = GradeValue.VERY_GOOD_PLUS, weight = 2),
            testGrade.copy(value = GradeValue.VERY_GOOD_MINUS, weight = 2),
            testGrade.copy(value = GradeValue.GOOD_PLUS, weight = 3),
            testGrade.copy(value = GradeValue.GOOD_MINUS, weight = 3),
            testGrade.copy(value = GradeValue.SATISFACTORY_PLUS, weight = 5),
            testGrade.copy(value = GradeValue.SATISFACTORY_MINUS, weight = 5),
            testGrade.copy(value = GradeValue.ACCEPTABLE_PLUS, weight = 8),
            testGrade.copy(value = GradeValue.ACCEPTABLE_MINUS, weight = 8),
            testGrade.copy(value = GradeValue.UNSATISFACTORY, weight = 13),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("2.50")
    }
})
