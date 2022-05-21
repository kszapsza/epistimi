package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.dto.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId
import java.time.LocalDate

class CourseFacade(
    private val courseAggregator: CourseAggregator,
    private val courseRegistrar: CourseRegistrar,
    private val courseRepository: CourseRepository,
    private val detailsDecorator: CourseDetailsDecorator,
) {
    fun getCourses(requesterUserId: UserId, classTeacherId: TeacherId?): List<CourseDetails> {
        return courseAggregator.getCourses(requesterUserId, classTeacherId)
            .map { course -> detailsDecorator.decorate(course) }
    }

    fun getCourse(courseId: CourseId, userId: UserId): CourseDetails {
        return courseAggregator.getCourse(courseId, userId)
            .let { course -> detailsDecorator.decorate(course) }
    }

    fun createCourse(userId: UserId, createRequest: CourseCreateRequest): CourseDetails {
        return courseRegistrar.createCourse(userId, createRequest)
            .let { course -> detailsDecorator.decorate(course) }
    }

    fun addStudent(courseId: CourseId, studentId: StudentId): Course {
        return courseRepository.findById(courseId)
            .also { course -> if (course.schoolYearEnd.isBefore(LocalDate.now())) throw CourseUnmodifiableException() }
            .let { it.copy(studentIds = it.studentIds + studentId) }
            .let { courseRepository.save(it) }
    }
}
