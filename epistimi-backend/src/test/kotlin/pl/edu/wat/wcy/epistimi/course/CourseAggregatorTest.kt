package pl.edu.wat.wcy.epistimi.course

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.TestUtils
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId

internal class CourseAggregatorTest : ShouldSpec({
    val courseRepository = mockk<CourseRepository>()
    val organizationContextProvider = mockk<OrganizationContextProvider>()

    val courseAggregator = CourseAggregator(
        courseRepository,
        organizationContextProvider,
    )

    val teacherStub = Teacher(
        id = TeacherId("teacher_id"),
        userId = TestData.Users.teacher.id!!,
        organizationId = TestData.organization.id!!,
        academicTitle = "dr",
    )

    val courseStub = Course(
        id = CourseId("course1"),
        organizationId = TestData.organization.id!!,
        code = Course.Code(
            number = "6",
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacherId = teacherStub.id!!,
        studentIds = emptyList(),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
        profession = null,
        profile = null,
        specialization = null,
    )

    should("return list of courses for organization administered by admin with provided id") {
        // given
        every { courseRepository.findAllWithFiltering(OrganizationId("organization_id"), null) } returns listOf(courseStub)
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization

        // when
        val courses = courseAggregator.getCourses(UserId("admin_user_id"), null)

        // then
        with(courses) {
            shouldHaveSize(1)
            shouldContain(courseStub)
        }
    }

    should("return empty list of courses if admin with provided id does not administer any organization") {
        // given
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns null

        // when
        val courses = courseAggregator.getCourses(UserId("admin_user_id"), null)

        // then
        courses.shouldBeEmpty()
    }

    should("return a single course by id") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationContextProvider.provide(UserId("admin_user_id")) } returns TestData.organization

        // when
        val course = courseAggregator.getCourse(CourseId("course_id"), UserId("admin_user_id"))

        // then
        course shouldBe courseStub
    }

    should("throw an exception if course with provided exists in organization not managed by current user") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns courseStub
        every { organizationContextProvider.provide(UserId("other_admin_id")) } returns null

        // expect
        shouldThrow<CourseNotFoundException> {
            courseAggregator.getCourse(CourseId("course_id"), UserId("other_admin_id"))
        }
    }
})
