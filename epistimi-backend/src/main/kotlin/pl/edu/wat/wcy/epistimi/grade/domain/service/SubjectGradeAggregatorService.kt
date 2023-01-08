package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGradePeriod
import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGrades
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGradesAverageSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentClassification
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.access.ClassificationGradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.ClassificationGradeRepository
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
    private val classificationGradeRepository: ClassificationGradeRepository,
    private val classificationGradeAccessValidator: ClassificationGradeAccessValidator,
) {
    /**
     * Retrieves subject grades and classification grades from repository.
     * If `studentIds` param was provided, only grades for those students will be provided.
     *
     * @param requester User requesting grades information.
     * @param studentIds Student ids to be included. If not provided, all students' grades will be retrieved.
     * @return A summary of subject grades.
     */
    fun getSubjectGrades(
        requester: User,
        subjectId: SubjectId,
        studentIds: List<StudentId>?,
    ): SubjectGrades {
        val subject = subjectFacade.getSubject(requester, subjectId)
        val subjectStudentsGrades = retrieveSubjectStudentsGrades(requester, subject, studentIds)
        val subjectStudentsClassificationGrades = retrieveSubjectStudentsClassificationGrades(requester, subject, studentIds)

        return SubjectGrades(
            id = subjectId,
            name = subject.name,
            students = subject.course.students
                .map { student ->
                    buildSubjectStudentGradesSummary(
                        student = student,
                        grades = subjectStudentsGrades[student] ?: emptyList(),
                        classificationGrades = subjectStudentsClassificationGrades[student] ?: emptyList(),
                    )
                }
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
        val filters = GradeFilters(
            subjectIds = listOf(subject.id!!),
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

    private fun retrieveSubjectStudentsClassificationGrades(
        requester: User,
        subject: Subject,
        studentIds: List<StudentId>?,
    ): Map<Student, List<ClassificationGrade>> {
        val filters = GradeFilters(
            subjectIds = listOf(subject.id!!),
            studentIds = studentIds
        )
        return retrieveClassificationGradesWithAuthorizationFiltering(requester, filters)
            .groupBy(ClassificationGrade::student)
    }

    private fun retrieveClassificationGradesWithAuthorizationFiltering(
        requester: User,
        filters: GradeFilters,
    ): List<ClassificationGrade> {
        return classificationGradeRepository.findAllWithFiltering(filters)
            .filter { grade -> classificationGradeAccessValidator.canRetrieve(requester, grade) }
            .sortedBy(ClassificationGrade::issuedAt)
    }

    private fun buildSubjectStudentGradesSummary(
        student: Student,
        grades: List<Grade>,
        classificationGrades: List<ClassificationGrade>,
    ): SubjectStudentGradesSummary {
        val classificationProposal = classificationGrades.firstOrNull { it.isProposal && it.period == ClassificationGradePeriod.SCHOOL_YEAR }
        val classificationFinal = classificationGrades.firstOrNull { !it.isProposal && it.period == ClassificationGradePeriod.SCHOOL_YEAR }

        return SubjectStudentGradesSummary(
            id = student.id!!,
            firstName = student.user.firstName,
            lastName = student.user.lastName,
            firstSemester = buildSubjectStudentSemesterGradesSummary(grades, classificationGrades, semesterNumber = 1),
            secondSemester = buildSubjectStudentSemesterGradesSummary(grades, classificationGrades, semesterNumber = 2),
            average = grades.weightedAverage(),
            classification = SubjectStudentClassification(
                proposal = classificationProposal,
                final = classificationFinal,
            ),
        )
    }

    private fun buildSubjectStudentSemesterGradesSummary(
        grades: List<Grade>,
        classificationGrades: List<ClassificationGrade>,
        semesterNumber: Int,
    ): SubjectStudentSemesterGradesSummary {
        val classificationPeriod = ClassificationGradePeriod.fromSemesterNumber(semesterNumber)
        val classificationProposal = classificationGrades.firstOrNull { it.isProposal && it.period == classificationPeriod }
        val classificationFinal = classificationGrades.firstOrNull { !it.isProposal && it.period == classificationPeriod }

        return grades
            .filter { grade -> grade.semester == semesterNumber }
            .let { semesterGrades ->
                SubjectStudentSemesterGradesSummary(
                    grades = semesterGrades,
                    average = semesterGrades.weightedAverage(),
                    classification = SubjectStudentClassification(
                        proposal = classificationProposal,
                        final = classificationFinal,
                    ),
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
