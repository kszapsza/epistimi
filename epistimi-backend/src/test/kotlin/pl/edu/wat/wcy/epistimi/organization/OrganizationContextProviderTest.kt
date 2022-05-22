package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

internal class OrganizationContextProviderTest : ShouldSpec({

    val organizationRepository = mockk<OrganizationRepository>()
    val userRepository = mockk<UserRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val studentRepository = mockk<StudentRepository>()
    val parentRepository = mockk<ParentRepository>()

    val organizationContextProvider = OrganizationContextProvider(
        organizationRepository,
        userRepository,
        teacherRepository,
        studentRepository,
        parentRepository,
    )

    should("provide an organization administered by provided ORGANIZATION_ADMIN") {
        // given
        every { userRepository.findById(UserId("organization_admin_id")) } returns TestData.Users.organizationAdmin
        every { organizationRepository.findFirstByAdminId(UserId("organization_admin_id")) } returns TestData.organization

        // when
        val organization = organizationContextProvider.provide(UserId("organization_admin_id"))

        // then
        organization shouldBe TestData.organization
    }

    should("provide an organization connected with provided TEACHER") {
        // given
        every { userRepository.findById(UserId("teacher_user_id")) } returns TestData.Users.teacher
        every { teacherRepository.findByUserId(UserId("teacher_user_id")) } returns TestData.teacher
        every { organizationRepository.findById(OrganizationId("organization_id")) } returns TestData.organization

        // when
        val organization = organizationContextProvider.provide(UserId("teacher_user_id"))

        // then
        organization shouldBe TestData.organization
    }

    // TODO: student, parent
})
