package pl.edu.wat.wcy.epistimi.subject.domain.service

import pl.edu.wat.wcy.epistimi.subject.domain.CourseSubjects
import pl.edu.wat.wcy.epistimi.subject.domain.SchoolYearSubjects
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectNotFoundException
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectsBySchoolYearAndCourse
import pl.edu.wat.wcy.epistimi.subject.domain.access.SubjectAccessValidator
import pl.edu.wat.wcy.epistimi.subject.domain.port.SubjectRepository
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class SubjectAggregatorService(
    private val subjectRepository: SubjectRepository,
    private val subjectAccessValidator: SubjectAccessValidator,
) {
    fun getSubject(
        contextUser: User,
        subjectId: SubjectId,
    ): Subject {
        return subjectRepository.findById(subjectId)
            .also { subject ->
                if (!subjectAccessValidator.canRetrieve(contextUser, subject)) {
                    throw SubjectNotFoundException(subject.id!!)
                }
            }
    }

    fun getSubjects(contextUser: User): SubjectsBySchoolYearAndCourse {
        return retrieveSubjectUserHasAccessTo(contextUser)
            .groupBySchoolYearAndCourse()
            .let(::SubjectsBySchoolYearAndCourse)
    }

    private fun retrieveSubjectUserHasAccessTo(contextUser: User): List<Subject> {
        val subjects = when (contextUser.role) {
            UserRole.EPISTIMI_ADMIN -> emptyList()
            UserRole.ORGANIZATION_ADMIN -> subjectRepository.findAllByOrganizationId(contextUser.organization!!.id!!)
            UserRole.TEACHER -> subjectRepository.findAllByTeacherUserId(contextUser.id!!)
            UserRole.STUDENT -> subjectRepository.findAllByStudentUserId(contextUser.id!!)
            UserRole.PARENT -> subjectRepository.findAllByParentUserId(contextUser.id!!)
        }
        return subjects
    }

    private fun List<Subject>.groupBySchoolYearAndCourse(): List<SchoolYearSubjects> {
        return this
            .groupBy { subject -> subject.course.schoolYear }
            .map { (schoolYear, schoolYearSubjects) ->
                SchoolYearSubjects(
                    schoolYear = schoolYear,
                    courses = schoolYearSubjects.groupByCourse(),
                )
            }
            .sortedByDescending { it.schoolYear }
    }

    private fun List<Subject>.groupByCourse(): List<CourseSubjects> {
        return this
            .groupBy { subject -> subject.course }
            .map { (course, courseSubjects) ->
                CourseSubjects(
                    id = course.id!!,
                    codeNumber = course.codeNumber,
                    codeLetter = course.codeLetter,
                    schoolYear = course.schoolYear,
                    classTeacher = course.classTeacher,
                    students = course.students.toList(),
                    subjects = courseSubjects.toList().sortedBy { it.name }, // note: user accessible subjects
                )
            }
            .sortedWith(compareBy(CourseSubjects::codeNumber, CourseSubjects::codeLetter))
    }
}
