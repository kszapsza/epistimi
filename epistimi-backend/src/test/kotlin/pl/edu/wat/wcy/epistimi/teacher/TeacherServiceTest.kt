package pl.edu.wat.wcy.epistimi.teacher

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.user.UserId

internal class TeacherServiceTest : ShouldSpec({

    val teacherRepository = mockk<TeacherRepository>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val teacherService = TeacherService(teacherRepository, organizationContextProvider)

    should("return empty list of teachers if there are no teachers in school administered by provided admin") {
        // given
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization
        every { teacherRepository.findAll(OrganizationId("organization_id")) } returns listOf()

        // when
        val teachers = teacherService.getTeachers(UserId("admin_user_id"))

        // then
        teachers.shouldBeEmpty()
    }

    should("return list of teachers in school administered by provided admin") {
        // given
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization
        every { teacherRepository.findAll(OrganizationId("organization_id")) } returns listOf(TestData.teacher)

        // when
        val teachers = teacherService.getTeachers(UserId("admin_user_id"))

        // then
        with(teachers) {
            shouldHaveSize(1)
            shouldContain(TestData.teacher)
        }
    }

})
