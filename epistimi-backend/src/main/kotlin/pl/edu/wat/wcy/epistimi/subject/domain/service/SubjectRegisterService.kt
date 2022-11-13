package pl.edu.wat.wcy.epistimi.subject.domain.service

import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectBadRequestException
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectRegisterRequest
import pl.edu.wat.wcy.epistimi.subject.domain.port.SubjectRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.user.domain.User

class SubjectRegisterService(
    private val subjectRepository: SubjectRepository,
    private val courseFacade: CourseFacade,
    private val teacherFacade: TeacherFacade,
) {
    fun registerSubject(
        contextUser: User,
        subjectRegisterRequest: SubjectRegisterRequest,
    ): Subject {
        val (courseId, teacherId, name) = subjectRegisterRequest

        val course = courseFacade.getCourse(contextUser, courseId)
        val teacher = teacherFacade.getTeacherById(contextUser, teacherId)
        validateCourseAndTeacher(contextUser.organization!!, course, teacher)

        return subjectRepository.save(
            Subject(course = course, teacher = teacher, name = name),
        )
    }

    private fun validateCourseAndTeacher(
        contextOrganization: Organization,
        course: Course,
        teacher: Teacher,
    ) {
        if (course.organization.id != contextOrganization.id) {
            throw SubjectBadRequestException("Course with id [${course.id}] was not found")
        }
        if (teacher.user.organization!!.id != contextOrganization.id) {
            throw SubjectBadRequestException("Teacher with id [${teacher.id}] was not found")
        }
    }
}
