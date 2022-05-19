package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.course.dto.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import java.time.LocalDate

class CourseFacade(
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository,
    private val organizationContextProvider: OrganizationContextProvider,
    private val detailsDecorator: CourseDetailsDecorator,
) {
    fun getCourses(userId: UserId): List<CourseDetails> {
        return organizationContextProvider.provide(userId)
            ?.let { organization -> courseRepository.findAll(organization.id!!) }
            ?.map { course -> detailsDecorator.decorate(course) }
            ?: emptyList()
    }

    fun getCourse(courseId: CourseId, userId: UserId): CourseDetails {
        val organizationContext = organizationContextProvider.provide(userId)
        val course = courseRepository.findById(courseId)

        if (organizationContext == null || course.organizationId != organizationContext.id) {
            throw CourseNotFoundException(courseId)
        }
        return detailsDecorator.decorate(course)
    }

    fun createCourse(userId: UserId, createRequest: CourseCreateRequest): CourseDetails {
        if (!createRequest.isSchoolYearTimeFrameValid()) {
            throw CourseBadRequestException("Invalid school year time frame")
        }
        return saveCourse(userId, createRequest)
            .let { course -> detailsDecorator.decorate(course) }
    }

    private fun CourseCreateRequest.isSchoolYearTimeFrameValid(): Boolean {
        return schoolYearBegin.isBefore(schoolYearEnd) &&
            schoolYearBegin.isBefore(schoolYearSemesterEnd) &&
            schoolYearSemesterEnd.isBefore(schoolYearEnd) &&
            schoolYearBegin.year == schoolYearEnd.year - 1
    }

    private fun saveCourse(
        userId: UserId,
        createRequest: CourseCreateRequest,
    ): Course {
        val organization = organizationContextProvider.provide(userId)
        val classTeacher = tryGetClassTeacher(createRequest.classTeacherId)

        if (organization == null) {
            throw CourseBadRequestException("User not managing any organization")
        }
        if (classTeacher.organizationId != organization.id) {
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
                organizationId = organization.id!!,
                code = Course.Code(
                    number = createRequest.codeNumber.toString(),
                    letter = createRequest.codeLetter,
                ),
                schoolYear = createRequest.formatSchoolYear,
                classTeacherId = classTeacher.id!!,
                studentIds = emptyList(),
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

    fun addStudent(courseId: CourseId, studentId: StudentId): Course {
        return courseRepository.findById(courseId)
            .also { course -> if (course.schoolYearEnd.isBefore(LocalDate.now())) throw CourseUnmodifiableException() }
            .let { it.copy(studentIds = it.studentIds + studentId) }
            .let { courseRepository.save(it) }
    }
}
