package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGrades
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGradesAverageSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.weightedAverage
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.User

class SubjectGradeAggregatorService(
    private val subjectFacade: SubjectFacade,
    private val gradeRepository: GradeRepository,
    private val gradeAccessValidator: GradeAccessValidator,
) {
    fun getSubjectGrades(
        requester: User,
        subjectId: SubjectId,
        studentIds: List<StudentId>?,
    ): SubjectGrades {
        val subject = subjectFacade.getSubject(requester, subjectId)
        val subjectStudentsGrades = retrieveSubjectStudentsGrades(requester, subject, studentIds)

        return SubjectGrades(
            id = subjectId,
            name = subject.name,
            students = subjectStudentsGrades
                .map(::buildSubjectStudentGradesSummary)
                .sortedWith(compareBy({ it.lastName }, { it.firstName })),
            average = subjectStudentsGrades.values
                .flatten()
                .let(::buildSubjectGradesAverageSummary)
        )
    }

    private fun retrieveSubjectStudentsGrades(
        requester: User,
        subject: Subject,
        studentIds: List<StudentId>?,
    ): Map<Student, List<Grade>> {
        return subject.course.students.associateWith { emptyList<Grade>() }
            .plus(retrieveSubjectStudentsWithGrades(requester, subject.id!!, studentIds))
    }

    private fun retrieveSubjectStudentsWithGrades(
        requester: User,
        subjectId: SubjectId,
        studentIds: List<StudentId>?,
    ): Map<Student, List<Grade>> {
        val filters = GradeFilters(
            subjectIds = listOf(subjectId),
            studentIds = studentIds
        )
        return retrieveGradesWithAuthorizationFiltering(requester, filters)
            .groupBy(Grade::student)
    }

    private fun retrieveGradesWithAuthorizationFiltering(
        requester: User,
        filters: GradeFilters,
    ): List<Grade> {
        return gradeRepository.findAllWithFiltering(filters)
            .filter { grade -> gradeAccessValidator.canRetrieve(requester, grade) }
            .sortedBy(Grade::issuedAt)
    }

    private fun buildSubjectStudentGradesSummary(
        subjectStudentGrades: Map.Entry<Student, List<Grade>>,
    ): SubjectStudentGradesSummary {
        return subjectStudentGrades.let { (student, studentGrades) ->
            SubjectStudentGradesSummary(
                id = student.id!!,
                firstName = student.user.firstName,
                lastName = student.user.lastName,
                firstSemester = buildSubjectStudentSemesterGradesSummary(studentGrades, semesterNumber = 1),
                secondSemester = buildSubjectStudentSemesterGradesSummary(studentGrades, semesterNumber = 2),
                average = studentGrades.weightedAverage(),
            )
        }
    }

    private fun buildSubjectStudentSemesterGradesSummary(
        studentGrades: List<Grade>,
        semesterNumber: Int,
    ): SubjectStudentSemesterGradesSummary {
        return studentGrades
            .filter { grade -> grade.semester == semesterNumber }
            .let { semesterGrades ->
                SubjectStudentSemesterGradesSummary(
                    grades = semesterGrades,
                    average = semesterGrades.weightedAverage(),
                )
            }
    }

    private fun buildSubjectGradesAverageSummary(
        allSubjectGrades: List<Grade>,
    ): SubjectGradesAverageSummary {
        return SubjectGradesAverageSummary(
            firstSemester = allSubjectGrades.filter { it.semester == 1 }.weightedAverage(),
            secondSemester = allSubjectGrades.filter { it.semester == 2 }.weightedAverage(),
            overall = allSubjectGrades.weightedAverage(),
        )
    }
}
