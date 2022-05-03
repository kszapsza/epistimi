package pl.edu.wat.wcy.epistimi.course

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.course.dto.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository,
    private val organizationContextProvider: OrganizationContextProvider,
) {
    fun getCourses(userId: UserId): List<Course> {
        return organizationContextProvider.provide(userId)
            ?.let { organization -> courseRepository.findAll(organization.id!!) }
            ?: listOf()
    }

    fun getCourse(courseId: CourseId, userId: UserId): Course {
        val organizationContext = organizationContextProvider.provide(userId)
        val course = courseRepository.findById(courseId)

        if (organizationContext == null || course.organization.id != organizationContext.id) {
            throw CourseNotFoundException(courseId)
        }
        return course
    }

    fun createCourse(userId: UserId, createRequest: CourseCreateRequest): Course {
        if (!createRequest.isSchoolYearTimeFrameValid()) {
            throw CourseBadRequestException("Invalid school year time frame")
        }
        return saveCourse(userId, createRequest)
    }

    private fun CourseCreateRequest.isSchoolYearTimeFrameValid(): Boolean {
        return schoolYearBegin.isBefore(schoolYearEnd)
                && schoolYearBegin.isBefore(schoolYearSemesterEnd)
                && schoolYearSemesterEnd.isBefore(schoolYearEnd)
                && schoolYearBegin.year == schoolYearEnd.year - 1
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
        if (classTeacher.organization.id != organization.id) {
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
                    number = createRequest.codeNumber.toString(),
                    letter = createRequest.codeLetter
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

    fun addStudent(courseId: CourseId, student: Student): Course {
        return courseRepository.findById(courseId)
            .let { it.copy(students = it.students + student) }
            .let { courseRepository.save(it) }
    }
}
