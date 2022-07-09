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
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId
import java.time.LocalDate

internal class CourseFacadeTest : ShouldSpec({
    val courseAggregator = mockk<CourseAggregator>()
    val courseRegistrar = mockk<CourseRegistrar>()
    val courseRepository = mockk<CourseRepository>()

    val courseFacade = CourseFacade(
        courseAggregator,
        courseRegistrar,
        courseRepository,
    )

    val teacherStub = Teacher(
        id = TeacherId("teacher_id"),
        user = TestData.Users.teacher,
        organization = TestData.organization,
        academicTitle = "dr",
    )

    val courseStub = Course(
        id = CourseId("course1"),
        organization = TestData.organization,
        code = Course.Code(
            number = 6,
            letter = "a"
        ),
        schoolYear = "2012/2013",
        classTeacher = teacherStub,
        students = emptyList(),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
        profession = null,
        profile = null,
        specialization = null,
    )

    val studentStub = Student(
        id = StudentId("student_id"),
        user = UserId("student_user_id"),
        organization = TestData.organization,
        parents = emptyList(),
    )

    should("add new student to existing course") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns
                courseStub.copy(schoolYearEnd = LocalDate.now().plusMonths(1))
        every { courseRepository.save(any()) } returnsArgument 0

        // when
        val updatedCourse = courseFacade.addStudent(CourseId("course_id"), studentStub.id!!)

        // then
        with(updatedCourse.students) {
            shouldHaveSize(1)
            shouldContain(studentStub)
        }
    }

    should("fail adding new student to existing course if it has already ended") {
        // given
        every { courseRepository.findById(CourseId("course_id")) } returns
                courseStub.copy(schoolYearEnd = LocalDate.now().minusMonths(3))

        // expect
        shouldThrow<CourseUnmodifiableException> {
            courseFacade.addStudent(CourseId("course_id"), studentStub.id!!)
        }
    }
})
