package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId
import java.time.LocalDate

class CourseFacade(
    private val courseAggregator: CourseAggregator,
    private val courseRegistrar: CourseRegistrar,
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository,
) {
    fun getCourses(requesterUserId: UserId, classTeacherId: TeacherId?): List<Course> {
        return courseAggregator.getCourses(requesterUserId, classTeacherId)
    }

    fun getCourse(courseId: CourseId, userId: UserId): Course {
        return courseAggregator.getCourse(courseId, userId)
    }

    fun createCourse(userId: UserId, createRequest: CourseCreateRequest): Course {
        return courseRegistrar.createCourse(userId, createRequest)
    }

    fun addStudent(courseId: CourseId, studentId: StudentId): Course {
        val addedStudent: Student = studentRepository.findById(studentId)
        return courseRepository.findById(courseId)
            .also { course -> if (course.schoolYearEnd.isBefore(LocalDate.now())) throw CourseUnmodifiableException() }
            .let { it.copy(students = it.students + addedStudent) }
            .let { courseRepository.save(it) }
    }
}
