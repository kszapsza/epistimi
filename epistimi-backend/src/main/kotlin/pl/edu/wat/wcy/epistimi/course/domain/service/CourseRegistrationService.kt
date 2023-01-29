package pl.edu.wat.wcy.epistimi.course.domain.service

import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseBadRequestException
import pl.edu.wat.wcy.epistimi.course.domain.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.domain.port.CourseRepository
import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.user.domain.User

class CourseRegistrationService(
    private val courseRepository: CourseRepository,
    private val teacherFacade: TeacherFacade,
) {
    companion object {
        private val logger by logger()
    }

    fun createCourse(
        contextUser: User,
        createRequest: CourseCreateRequest,
    ): Course {
        if (!createRequest.isSchoolYearTimeFrameValid()) {
            throw CourseBadRequestException("Invalid school year time frame")
        }
        return saveCourse(contextUser, createRequest)
    }

    private fun CourseCreateRequest.isSchoolYearTimeFrameValid(): Boolean {
        return schoolYearBegin.isBefore(schoolYearEnd) &&
            schoolYearBegin.isBefore(schoolYearSemesterEnd) &&
            schoolYearSemesterEnd.isBefore(schoolYearEnd) &&
            schoolYearBegin.year == schoolYearEnd.year - 1
    }

    private fun saveCourse(
        contextUser: User,
        createRequest: CourseCreateRequest,
    ): Course {
        val classTeacher = tryGetClassTeacher(contextUser, createRequest.classTeacherId)

        if (classTeacher.user.organization?.id != contextUser.organization!!.id) {
            logger.warn("Attempted to register course with class teacher from other organization")
            throw CourseBadRequestException("Provided class teacher is not associated with your organization")
        }
        return saveCourse(contextUser.organization, createRequest, classTeacher)
    }

    private fun tryGetClassTeacher(
        contextUser: User,
        classTeacherId: TeacherId,
    ): Teacher {
        return try {
            teacherFacade.getTeacherById(contextUser, classTeacherId)
        } catch (e: TeacherNotFoundException) {
            throw CourseBadRequestException("Teacher with id ${classTeacherId.value} was not found")
        }
    }

    private fun saveCourse(
        organization: Organization,
        createRequest: CourseCreateRequest,
        classTeacher: Teacher,
    ): Course {
        return courseRepository.save(
            Course(
                id = null,
                organization = organization,
                codeNumber = createRequest.codeNumber,
                codeLetter = createRequest.codeLetter,
                classTeacher = classTeacher,
                students = emptySet(),
                subjects = emptySet(),
                schoolYearBegin = createRequest.schoolYearBegin,
                schoolYearSemesterEnd = createRequest.schoolYearSemesterEnd,
                schoolYearEnd = createRequest.schoolYearEnd,
                profile = createRequest.profile,
                profession = createRequest.profession,
                specialization = createRequest.specialization,
            ),
        )
    }
}
