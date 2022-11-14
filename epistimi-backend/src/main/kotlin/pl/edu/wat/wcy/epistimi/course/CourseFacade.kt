package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.course.domain.service.CourseAggregatorService
import pl.edu.wat.wcy.epistimi.course.domain.service.CourseRegistrationService
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.user.domain.User

class CourseFacade(
    private val courseAggregatorService: CourseAggregatorService,
    private val courseRegistrationService: CourseRegistrationService,
) {
    fun getCourses(contextUser: User, classTeacherId: TeacherId?): List<Course> {
        return courseAggregatorService.getCourses(contextUser.organization, classTeacherId)
    }

    fun getCourse(contextUser: User, courseId: CourseId): Course {
        return courseAggregatorService.getCourse(contextUser.organization, courseId)
    }

    fun createCourse(contextUser: User, createRequest: CourseCreateRequest): Course {
        return courseRegistrationService.createCourse(contextUser.organization, createRequest)
    }
}
