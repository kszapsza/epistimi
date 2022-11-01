package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.User
import java.time.LocalDate

class CourseFacade(
    private val courseAggregator: CourseAggregator,
    private val courseRegistrar: CourseRegistrar,
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository,
) {
    fun getCourses(contextUser: User, classTeacherId: TeacherId?): List<Course> {
        return courseAggregator.getCourses(contextUser.organization, classTeacherId)
    }

    fun getCourse(contextUser: User, courseId: CourseId): Course {
        return courseAggregator.getCourse(contextUser.organization, courseId)
    }

    fun createCourse(contextUser: User, createRequest: CourseCreateRequest): Course {
        return courseRegistrar.createCourse(contextUser.organization, createRequest)
    }

    fun addStudent(courseId: CourseId, studentId: StudentId): Course {
        val addedStudent: Student = studentRepository.findById(studentId)
        return courseRepository.findById(courseId)
            .also { course -> if (course.schoolYearEnd.isBefore(LocalDate.now())) throw CourseUnmodifiableException() }
            .also { it.students + addedStudent }
            .let { courseRepository.save(it) }
    }
}
