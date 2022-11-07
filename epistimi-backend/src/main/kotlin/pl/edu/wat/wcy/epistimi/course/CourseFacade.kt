package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.User

class CourseFacade(
    private val courseAggregator: CourseAggregator,
    private val courseRegistrar: CourseRegistrar,
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
}
