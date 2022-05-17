package pl.edu.wat.wcy.epistimi.course.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseNotFoundException
import pl.edu.wat.wcy.epistimi.course.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

@Repository
class CourseDbRepository(
    private val courseMongoDbRepository: CourseMongoDbRepository,
    private val organizationRepository: OrganizationRepository,
) : CourseRepository {

    override fun findById(courseId: CourseId): Course {
        return courseMongoDbRepository.findById(courseId.value)
            .map { it.toDomain() }
            .orElseThrow { CourseNotFoundException(courseId) }
    }

    override fun findAll(organizationId: OrganizationId): List<Course> {
        if (!organizationRepository.exists(organizationId)) {
            throw OrganizationNotFoundException(organizationId)
        }
        return courseMongoDbRepository.findAllByOrganizationId(organizationId.value)
            .map { it.toDomain() }
    }

    private fun CourseMongoDbDocument.toDomain() = Course(
        id = CourseId(id!!),
        organizationId = OrganizationId(organizationId),
        code = Course.Code(number = code.number, letter = code.letter),
        schoolYear = schoolYear,
        classTeacherId = TeacherId(classTeacherId),
        studentIds = studentIds.map { StudentId(it) },
        schoolYearBegin = schoolYearBegin,
        schoolYearSemesterEnd = schoolYearSemesterEnd,
        schoolYearEnd = schoolYearEnd,
        profile = profile,
        profession = profession,
        specialization = specialization,
    )

    override fun save(course: Course): Course {
        return course.toMongoDbDocument()
            .let { courseMongoDbRepository.save(it) }
            .toDomain()
    }

    private fun Course.toMongoDbDocument() = CourseMongoDbDocument(
        id = id?.value,
        organizationId = organizationId.value,
        code = CourseMongoDbDocument.Code(number = code.number, letter = code.letter),
        schoolYear = schoolYear,
        classTeacherId = classTeacherId.value,
        studentIds = studentIds.map { it.value },
        schoolYearBegin = schoolYearBegin,
        schoolYearSemesterEnd = schoolYearSemesterEnd,
        schoolYearEnd = schoolYearEnd,
        profile = profile,
        profession = profession,
        specialization = specialization,
    )
}
