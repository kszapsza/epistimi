package pl.edu.wat.wcy.epistimi.course

import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.parent.ParentDetails
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.student.StudentDetails
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetails
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

class CourseDetailsDecorator(
    private val teacherRepository: TeacherRepository,
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val parentRepository: ParentRepository,
) {
    fun decorate(course: Course) = with(course) {
        CourseDetails(
            id = id,
            organization = organizationRepository.findById(organizationId),
            code = Course.Code(number = code.number, letter = code.letter),
            schoolYear = schoolYear,
            classTeacher = retrieveClassTeacher(classTeacherId),
            students = retrieveStudents(studentIds),
            schoolYearBegin = schoolYearBegin,
            schoolYearSemesterEnd = schoolYearSemesterEnd,
            schoolYearEnd = schoolYearEnd,
            profile = profile,
            profession = profession,
            specialization = specialization,
        )
    }

    private fun retrieveClassTeacher(classTeacherId: TeacherId): TeacherDetails {
        return teacherRepository.findById(classTeacherId)
            .let { teacher ->
                TeacherDetails(
                    id = teacher.id,
                    user = userRepository.findById(teacher.userId),
                    organization = organizationRepository.findById(teacher.organizationId),
                    academicTitle = teacher.academicTitle,
                )
            }
    }

    private fun retrieveStudents(studentIds: List<StudentId>): List<StudentDetails> {
        return studentRepository.findByIds(studentIds)
            .map { student ->
                StudentDetails(
                    id = student.id,
                    user = userRepository.findById(student.userId),
                    organization = organizationRepository.findById(student.organizationId),
                    parents = retrieveParents(student.parentsIds),
                )
            }
    }

    private fun retrieveParents(parentIds: List<ParentId>): List<ParentDetails> {
        return parentRepository.findByIds(parentIds)
            .map { parent ->
                ParentDetails(
                    id = parent.id,
                    user = userRepository.findById(parent.userId),
                    organization = organizationRepository.findById(parent.organizationId),
                )
            }
    }
}
