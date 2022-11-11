package pl.edu.wat.wcy.epistimi.grade

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.TestUtils
import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeValue
import pl.edu.wat.wcy.epistimi.grade.domain.weightedAverage
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

    fun testGrade(
        value: GradeValue,
        weight: Int,
        countTowardsAverage: Boolean = true,
    ) = Grade(
        id = null,
        subject = TestData.subject,
        student = TestData.student,
        issuedBy = TestData.teacher,
        issuedAt = TestUtils.parseDateTime("2012-09-10 14:05"),
        updatedAt = null,
        value = value,
        weight = weight,
        category = testGradeCategory,
        countTowardsAverage = countTowardsAverage,
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
            testGrade(value = GradeValue.SATISFACTORY, weight = 3),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("3.00")
    }

    should("calculate weighted average for multiple element grade collection") {
        // given
        val grades = listOf(
            testGrade(value = GradeValue.EXCELLENT, weight = 13),
            testGrade(value = GradeValue.VERY_GOOD, weight = 8),
            testGrade(value = GradeValue.GOOD, weight = 5),
            testGrade(value = GradeValue.SATISFACTORY, weight = 3),
            testGrade(value = GradeValue.ACCEPTABLE, weight = 2),
            testGrade(value = GradeValue.UNSATISFACTORY, weight = 1),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("4.75")
    }

    should("calculate weighted average ignoring grades marked as not counted towards average") {
        // given
        val grades = listOf(
            testGrade(value = GradeValue.GOOD, weight = 3, countTowardsAverage = false),
            testGrade(value = GradeValue.EXCELLENT, weight = 2),
            testGrade(value = GradeValue.UNSATISFACTORY, weight = 1),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("4.33")
    }

    should("calculate weighted average ignoring grades without numeric values") {
        // given
        val grades = listOf(
            testGrade(value = GradeValue.NO_ASSIGNMENT, weight = 3),
            testGrade(value = GradeValue.UNPREPARED, weight = 3),
            testGrade(value = GradeValue.UNCLASSIFIED, weight = 3),
            testGrade(value = GradeValue.ATTENDED, weight = 3),
            testGrade(value = GradeValue.DID_NOT_ATTEND, weight = 3),
            testGrade(value = GradeValue.PASSED, weight = 3),
            testGrade(value = GradeValue.FAILED, weight = 3),
            testGrade(value = GradeValue.ABSENT, weight = 3),
            testGrade(value = GradeValue.EXEMPT, weight = 3),
            testGrade(value = GradeValue.UNSATISFACTORY, weight = 2),
            testGrade(value = GradeValue.EXCELLENT, weight = 2),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("3.50")
    }

    should("calculate weighted average ignoring \"minuses\" and \"pluses\"") {
        // given
        val grades = listOf(
            testGrade(value = GradeValue.EXCELLENT, weight = 1),
            testGrade(value = GradeValue.VERY_GOOD_PLUS, weight = 2),
            testGrade(value = GradeValue.VERY_GOOD_MINUS, weight = 2),
            testGrade(value = GradeValue.GOOD_PLUS, weight = 3),
            testGrade(value = GradeValue.GOOD_MINUS, weight = 3),
            testGrade(value = GradeValue.SATISFACTORY_PLUS, weight = 5),
            testGrade(value = GradeValue.SATISFACTORY_MINUS, weight = 5),
            testGrade(value = GradeValue.ACCEPTABLE_PLUS, weight = 8),
            testGrade(value = GradeValue.ACCEPTABLE_MINUS, weight = 8),
            testGrade(value = GradeValue.UNSATISFACTORY, weight = 13),
        )

        // when
        val weightedAverage = grades.weightedAverage()

        // then
        weightedAverage shouldBe BigDecimal("2.50")
    }
})
