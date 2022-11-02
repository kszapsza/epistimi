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
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId
import java.util.UUID

internal class CourseAggregatorTest : ShouldSpec({

    val courseRepository = mockk<CourseRepository>()
    val courseAggregator = CourseAggregator(courseRepository)

    val teacherId = TeacherId(UUID.randomUUID())
    val courseId = CourseId(UUID.randomUUID())

    val teacherStub = Teacher(
        id = teacherId,
        user = TestData.Users.teacher,
        academicTitle = "dr",
    )

    val courseStub = Course(
        id = courseId,
        organization = TestData.organization,
        codeNumber = 6,
        codeLetter = "a",
        classTeacher = teacherStub,
        students = emptySet(),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
        profession = null,
        profile = null,
        specialization = null,
    )

    should("return list of courses for organization administered by admin with provided id") {
        // given
        every { courseRepository.findAllWithFiltering(TestData.organization.id!!, null) } returns listOf(courseStub)

        // when
        val courses = courseAggregator.getCourses(TestData.organization, null)

        // then
        with(courses) {
            shouldHaveSize(1)
            shouldContain(courseStub)
        }
    }

    should("return empty list of courses if admin with provided id does not administer any organization") {
        // given
        val adminUserId = UserId(UUID.randomUUID())

        // when
        val courses = courseAggregator.getCourses(TestData.organization, null)

        // then
        courses.shouldBeEmpty()
    }

    should("return a single course by id") {
        // given
        every { courseRepository.findById(courseId) } returns courseStub

        // when
        val course = courseAggregator.getCourse(TestData.organization, courseId)

        // then
        course shouldBe courseStub
    }

    should("throw an exception if course with provided exists in organization not managed by current user") {
        // given
        val otherAdminId = UserId(UUID.randomUUID())
        every { courseRepository.findById(courseId) } returns courseStub

        // expect
        shouldThrow<CourseNotFoundException> {
            courseAggregator.getCourse(courseId, otherAdminId)
        }
    }
})
