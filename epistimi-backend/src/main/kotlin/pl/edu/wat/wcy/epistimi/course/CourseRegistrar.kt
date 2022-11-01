package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository

class CourseRegistrar(
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository,
) {
    companion object {
        private val logger by logger()
    }

    fun createCourse(
        contextOrganization: Organization,
        createRequest: CourseCreateRequest,
    ): Course {
        if (!createRequest.isSchoolYearTimeFrameValid()) {
            throw CourseBadRequestException("Invalid school year time frame")
        }
        return saveCourse(contextOrganization, createRequest)
    }

    private fun CourseCreateRequest.isSchoolYearTimeFrameValid(): Boolean {
        return schoolYearBegin.isBefore(schoolYearEnd) &&
                schoolYearBegin.isBefore(schoolYearSemesterEnd) &&
                schoolYearSemesterEnd.isBefore(schoolYearEnd) &&
                schoolYearBegin.year == schoolYearEnd.year - 1
    }

    private fun saveCourse(
        contextOrganization: Organization,
        createRequest: CourseCreateRequest,
    ): Course {
        val classTeacher = tryGetClassTeacher(createRequest.classTeacherId)

        if (classTeacher.user.organization.id != contextOrganization.id) {
            logger.warn("Attempted to register course with class teacher from other organization")
            throw CourseBadRequestException("Provided class teacher is not associated with your organization")
        }
        return saveCourse(contextOrganization, createRequest, classTeacher)
    }

    private fun tryGetClassTeacher(classTeacherId: TeacherId): Teacher {
        return try {
            teacherRepository.findById(classTeacherId)
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
