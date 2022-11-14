package pl.edu.wat.wcy.epistimi.teacher

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.teacher.domain.service.TeacherAggregatorService

internal class TeacherAggregatorTest : ShouldSpec({

    val teacherRepository = mockk<TeacherRepository>()
    val teacherAggregatorService = TeacherAggregatorService(teacherRepository)

    val organizationAdminId = TestData.Users.organizationAdmin.id!!
    val organizationId = TestData.organization.id!!

    should("return empty list of teachers if there are no teachers in school administered by provided admin") {
        // given
        every { teacherRepository.findAll(organizationId) } returns emptyList()

        // when
        val teachers = teacherAggregatorService.getTeachers(organizationAdminId)

        // then
        teachers.shouldBeEmpty()
    }

    should("return list of teachers in school administered by provided admin") {
        // given
        every { teacherRepository.findAll(organizationId) } returns listOf(TestData.teacher)

        // when
        val teachers = teacherAggregatorService.getTeachers(organizationAdminId)

        // then
        with(teachers) {
            shouldHaveSize(1)
            shouldContain(TestData.teacher)
        }
    }
})
