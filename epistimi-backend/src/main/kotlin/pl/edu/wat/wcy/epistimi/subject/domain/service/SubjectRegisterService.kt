package pl.edu.wat.wcy.epistimi.subject.domain.service

import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectBadRequestException
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectRegisterRequest
import pl.edu.wat.wcy.epistimi.subject.domain.port.SubjectRepository
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository

class SubjectRegisterService(
    private val subjectRepository: SubjectRepository,
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository,
) {

    fun registerSubject(
        contextOrganization: Organization,
        subjectRegisterRequest: SubjectRegisterRequest,
    ): Subject {
        val (courseId, teacherId, name) = subjectRegisterRequest

        val course = courseRepository.findById(courseId)
        val teacher = teacherRepository.findById(teacherId)
        validateCourseAndTeacher(contextOrganization, course, teacher)

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
