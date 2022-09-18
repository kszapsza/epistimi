package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.dto.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.logger
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

class CourseRegistrar(
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository,
    private val organizationContextProvider: OrganizationContextProvider,
) {
    companion object {
        private val logger by logger()
    }

    fun createCourse(userId: UserId, createRequest: CourseCreateRequest): Course {
        if (!createRequest.isSchoolYearTimeFrameValid()) {
            throw CourseBadRequestException("Invalid school year time frame")
        }
        return saveCourse(userId, createRequest)
    }

    private fun CourseCreateRequest.isSchoolYearTimeFrameValid(): Boolean {
        return schoolYearBegin.isBefore(schoolYearEnd) &&
                schoolYearBegin.isBefore(schoolYearSemesterEnd) &&
                schoolYearSemesterEnd.isBefore(schoolYearEnd) &&
                schoolYearBegin.year == schoolYearEnd.year - 1
    }

    private fun saveCourse(userId: UserId, createRequest: CourseCreateRequest): Course {
        val organization = organizationContextProvider.provide(userId)
        val classTeacher = tryGetClassTeacher(createRequest.classTeacherId)

        if (organization == null) {
            throw CourseBadRequestException("User not managing any organization")
        }
        if (classTeacher.organization.id != organization.id) {
            logger.warn("Attempted to register course with class teacher from other organization")
            throw CourseBadRequestException("Provided class teacher is not associated with your organization")
        }

        return saveCourse(organization, createRequest, classTeacher)
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
                code = Course.Code(
                    number = createRequest.codeNumber,
                    letter = createRequest.codeLetter,
                ),
                schoolYear = createRequest.formatSchoolYear,
                classTeacher = classTeacher,
                students = emptyList(),
                schoolYearBegin = createRequest.schoolYearBegin,
                schoolYearSemesterEnd = createRequest.schoolYearSemesterEnd,
                schoolYearEnd = createRequest.schoolYearEnd,
                profile = createRequest.profile,
                profession = createRequest.profession,
                specialization = createRequest.specialization,
            )
        )
    }

    private val CourseCreateRequest.formatSchoolYear
        get() = "${schoolYearBegin.year}/${schoolYearEnd.year}"
}
