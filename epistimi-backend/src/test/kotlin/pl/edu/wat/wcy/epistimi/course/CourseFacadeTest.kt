package pl.edu.wat.wcy.epistimi.course

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.TestUtils
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import java.time.LocalDate
import java.util.UUID

internal class CourseFacadeTest : ShouldSpec({
    val courseAggregator = mockk<CourseAggregator>()
    val courseRegistrar = mockk<CourseRegistrar>()
    val courseRepository = mockk<CourseRepository>()
    val studentRepository = mockk<StudentRepository>()

    val courseFacade = CourseFacade(
        courseAggregator,
        courseRegistrar,
        courseRepository,
        studentRepository,
    )

    val courseId = CourseId(UUID.randomUUID())

    val courseStub = Course(
        id = courseId,
        organization = TestData.organization,
        code = Course.Code(
            number = 6,
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacher = TestData.teacher,
        students = emptyList(),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
        profession = null,
        profile = null,
        specialization = null,
    )

    should("add new student to existing course") {
        // given
        every { courseRepository.findById(courseId) } returns
                courseStub.copy(schoolYearEnd = LocalDate.now().plusMonths(1))
        every { courseRepository.save(any()) } returnsArgument 0

        // when
        val updatedCourse = courseFacade.addStudent(courseId, TestData.student.id!!)

        // then
        with(updatedCourse.students) {
            shouldHaveSize(1)
            shouldContain(TestData.student)
        }
    }

    should("fail adding new student to existing course if it has already ended") {
        // given
        every { courseRepository.findById(courseId) } returns
                courseStub.copy(schoolYearEnd = LocalDate.now().minusMonths(3))

        // expect
        shouldThrow<CourseUnmodifiableException> {
            courseFacade.addStudent(courseId, TestData.student.id!!)
        }
    }
})
