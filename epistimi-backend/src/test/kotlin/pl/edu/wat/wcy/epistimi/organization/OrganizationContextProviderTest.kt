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
        val organizationAdminId = TestData.Users.organizationAdmin.id!!
        every { userRepository.findById(organizationAdminId) } returns TestData.Users.organizationAdmin
        every { organizationRepository.findFirstByAdminId(organizationAdminId) } returns TestData.organization

        // when
        val organization = organizationContextProvider.provide(organizationAdminId)

        // then
        organization shouldBe TestData.organization
    }

    should("provide an organization connected with provided TEACHER") {
        // given
        val teacherUserId = TestData.Users.teacher.id!!
        val organizationId = TestData.organization.id!!

        every { userRepository.findById(teacherUserId) } returns TestData.Users.teacher
        every { teacherRepository.findByUserId(teacherUserId) } returns TestData.teacher
        every { organizationRepository.findById(organizationId) } returns TestData.organization

        // when
        val organization = organizationContextProvider.provide(teacherUserId)

        // then
        organization shouldBe TestData.organization
    }

    should("provide an organization connected with provided STUDENT") {
        // given
        val studentUserId = TestData.Users.student.id!!
        val organizationId = TestData.organization.id!!

        every { userRepository.findById(studentUserId) } returns TestData.Users.student
        every { studentRepository.findByUserId(studentUserId) } returns TestData.student
        every { organizationRepository.findById(organizationId) } returns TestData.organization

        // when
        val organization = organizationContextProvider.provide(studentUserId)

        // then
        organization shouldBe TestData.organization
    }

    should("provide an organization connected with provided PARENT") {
        // given
        val parentUserId = TestData.Users.parent.id!!
        val organizationId = TestData.organization.id!!

        every { userRepository.findById(parentUserId) } returns TestData.Users.parent
        every { parentRepository.findByUserId(parentUserId) } returns TestData.parent
        every { organizationRepository.findById(organizationId) } returns TestData.organization

        // when
        val organization = organizationContextProvider.provide(parentUserId)

        // then
        organization shouldBe TestData.organization
    }

})
