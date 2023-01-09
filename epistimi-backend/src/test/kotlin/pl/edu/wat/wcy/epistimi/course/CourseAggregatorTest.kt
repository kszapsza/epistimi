package pl.edu.wat.wcy.epistimi.course

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.course.domain.CourseNotFoundException
import pl.edu.wat.wcy.epistimi.course.domain.port.CourseRepository
import pl.edu.wat.wcy.epistimi.course.domain.service.CourseAggregatorService
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import java.util.UUID

internal class CourseAggregatorTest : ShouldSpec({

    val courseRepository = mockk<CourseRepository>()
    val courseAggregatorService = CourseAggregatorService(courseRepository)

    should("return list of courses for organization administered by admin with provided id") {
        // given
        val organization = TestData.organization()
        val course = TestData.course()

        // and
        every { courseRepository.findAllWithFiltering(organization.id!!, null) } returns listOf(course)

        // when
        val courses = courseAggregatorService.getCourses(organization, null)

        // then
        with(courses) {
            shouldHaveSize(1)
            shouldContain(course)
        }
    }

    should("return a single course by id") {
        // given
        val organization = TestData.organization()
        val courseStub = TestData.course(organization = organization)

        // and
        every { courseRepository.findById(courseStub.id!!) } returns courseStub

        // when
        val course = courseAggregatorService.getCourse(organization, courseStub.id!!)

        // then
        course shouldBe courseStub
    }

    should("throw an exception if course with provided exists in organization not managed by current user") {
        // given
        val organization = TestData.organization(id = OrganizationId(UUID.randomUUID()))
        val otherOrganization = TestData.organization(id = OrganizationId(UUID.randomUUID()))
        val course = TestData.course(organization = organization)

        every { courseRepository.findById(course.id!!) } returns course

        // expect
        shouldThrow<CourseNotFoundException> {
            courseAggregatorService.getCourse(otherOrganization, course.id!!)
        }
    }
})
