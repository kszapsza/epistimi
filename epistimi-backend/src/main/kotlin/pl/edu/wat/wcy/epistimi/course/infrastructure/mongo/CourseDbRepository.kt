package pl.edu.wat.wcy.epistimi.course.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository

@Repository
class CourseDbRepository(
    private val courseMongoDbRepository: CourseMongoDbRepository,
    private val organizationRepository: OrganizationRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
) : CourseRepository {

    override fun findAll(organizationId: OrganizationId): List<Course> {
        if (!organizationRepository.exists(organizationId)) {
            throw OrganizationNotFoundException(organizationId)
        }
        return courseMongoDbRepository.findAllByOrganizationId(organizationId.value)
            .map { it.toDomain() }
    }

    private fun CourseMongoDbDocument.toDomain() = Course(
        id = CourseId(id!!),
        organization = organizationRepository.findById(OrganizationId(organizationId)),
        code = Course.Code(number = code.number, letter = code.letter),
        schoolYear = schoolYear,
        classTeacher = teacherRepository.findById(TeacherId(classTeacherId)),
        students = studentRepository.findByIds(studentIds.map { StudentId(it) })
    )

    override fun save(course: Course): Course {
        return course.toMongoDbDocument()
            .let { courseMongoDbRepository.save(it) }
            .toDomain()
    }

    private fun Course.toMongoDbDocument() = CourseMongoDbDocument(
        id = id?.value,
        organizationId = organization.id!!.value,
        code = CourseMongoDbDocument.Code(number = code.number, letter = code.letter),
        schoolYear = schoolYear,
        classTeacherId = classTeacher.id!!.value,
        studentIds = students.map { it.id!!.value },
    )
}
