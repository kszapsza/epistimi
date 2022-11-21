package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGrades
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGradesAverage
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentsGrades
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGrades
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGradesAverageSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.weightedAverage
import pl.edu.wat.wcy.epistimi.parent.ParentFacade
import pl.edu.wat.wcy.epistimi.student.StudentFacade
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class GradeAggregatorService(
    private val gradeRepository: GradeRepository,
    private val gradeAccessValidator: GradeAccessValidator,
    private val studentFacade: StudentFacade,
    private val subjectFacade: SubjectFacade,
    private val parentFacade: ParentFacade,
) {
    fun getGradeById(requester: User, gradeId: GradeId): Grade {
        return gradeRepository.findById(gradeId)
            .also { grade ->
                if (gradeAccessValidator.canRetrieve(requester, grade)) {
                    throw GradeNotFoundException(grade.id!!)
                }
            }
    }

    fun getStudentGrades(
        requester: User,
        subjectIds: List<SubjectId>?,
    ): StudentsGrades {
        val students = getContextStudents(requester)
        val studentsGrades = students.map { student -> retrieveStudentSubjectsGrades(requester, student, subjectIds) }

        return students.zip(studentsGrades)
            .map { (student, studentSubjectsGrades) ->
                StudentGrades(
                    id = student.id!!,
                    firstName = student.user.firstName,
                    lastName = student.user.lastName,
                    subjects = studentSubjectsGrades
                        .map(::buildSubjectGradesSummary)
                        .sortedBy(StudentSubjectGradesSummary::name)
                )
            }
            .let(::StudentsGrades)
    }

    private fun getContextStudents(requester: User): List<Student> {
        return when (requester.role) {
            UserRole.STUDENT -> listOf(studentFacade.getStudentByUserId(requester, requester.id!!))
            UserRole.PARENT -> parentFacade.getByUserId(requester.id!!).students
            else -> emptyList()
        }
    }

    private fun retrieveStudentSubjectsGrades(
        requester: User,
        student: Student,
        subjectIds: List<SubjectId>?,
    ): Map<Subject, List<Grade>> {
        return student.course.subjects.associateWith { emptyList<Grade>() }
            .plus(retrieveStudentSubjectsWithGrades(requester, student, subjectIds))
    }

    private fun retrieveStudentSubjectsWithGrades(
        requester: User,
        student: Student,
        subjectIds: List<SubjectId>?,
    ): Map<Subject, List<Grade>> {
        val filters = GradeFilters(
            studentIds = listOf(student.id!!),
            subjectIds = subjectIds,
        )
        return retrieveGradesWithAuthorizationFiltering(requester, filters)
            .groupBy(Grade::subject)
    }

    private fun retrieveGradesWithAuthorizationFiltering(
        requester: User,
        filters: GradeFilters,
    ): List<Grade> {
        return gradeRepository.findAllWithFiltering(filters)
            .filter { grade -> gradeAccessValidator.canRetrieve(requester, grade) }
            .sortedBy(Grade::issuedAt)
    }

    private fun buildSubjectGradesSummary(
        studentSubjectGrades: Map.Entry<Subject, List<Grade>>,
    ): StudentSubjectGradesSummary {
        val (subject, studentGrades) = studentSubjectGrades
        val allSubjectGrades = retrieveSubjectAllStudentsGrades(subject.id!!)

        return StudentSubjectGradesSummary(
            id = subject.id,
            name = subject.name,
            firstSemester = buildStudentSubjectSemesterGradesSummary(studentGrades, allSubjectGrades, semesterNumber = 1),
            secondSemester = buildStudentSubjectSemesterGradesSummary(studentGrades, allSubjectGrades, semesterNumber = 2),
            average = StudentGradesAverage(
                student = studentGrades.weightedAverage(),
                subject = allSubjectGrades.weightedAverage(),
            ),
        )
    }

    private fun retrieveSubjectAllStudentsGrades(subjectId: SubjectId): List<Grade> {
        val filters = GradeFilters(
            subjectIds = listOf(subjectId),
            studentIds = null,
        )
        return gradeRepository.findAllWithFiltering(filters)
    }

    private fun buildStudentSubjectSemesterGradesSummary(
        studentGrades: List<Grade>,
        allSubjectGrades: List<Grade>,
        semesterNumber: Int,
    ): StudentSubjectSemesterGradesSummary {
        val studentSemesterGrades = studentGrades.filter { grade -> grade.semester == semesterNumber }
        val allSubjectSemesterGrades = allSubjectGrades.filter { grade -> grade.semester == semesterNumber }

        return StudentSubjectSemesterGradesSummary(
            grades = studentSemesterGrades,
            average = StudentGradesAverage(
                student = studentSemesterGrades.weightedAverage(),
                subject = allSubjectSemesterGrades.weightedAverage(),
            ),
        )
    }

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
