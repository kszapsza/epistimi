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
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

internal class TeacherFacadeTest : ShouldSpec({

    val teacherRepository = mockk<TeacherRepository>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()
    val teacherDetailsDecorator = mockk<TeacherDetailsDecorator>()

    val teacherFacade = TeacherFacade(
        teacherRepository,
        organizationContextProvider,
        teacherDetailsDecorator,
    )

    should("return empty list of teachers if there are no teachers in school administered by provided admin") {
        // given
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization
        every { teacherRepository.findAll(OrganizationId("organization_id")) } returns emptyList()

        // when
        val teachers = teacherFacade.getTeachers(UserId("admin_user_id"))

        // then
        teachers.shouldBeEmpty()
    }

    should("return list of teachers in school administered by provided admin") {
        // given
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization
        every { teacherRepository.findAll(OrganizationId("organization_id")) } returns listOf(TestData.teacher)
        every { teacherDetailsDecorator.decorate(TestData.teacher) } returns TestData.teacherDetails

        // when
        val teachers = teacherFacade.getTeachers(UserId("admin_user_id"))

        // then
        with(teachers) {
            shouldHaveSize(1)
            shouldContain(TestData.teacherDetails)
        }
    }
})
