package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGradePeriod
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGradePeriod.SCHOOL_YEAR
import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGrades
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGradesAverage
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectClassification
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentsGrades
import pl.edu.wat.wcy.epistimi.grade.domain.access.ClassificationGradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.ClassificationGradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.weightedAverage
import pl.edu.wat.wcy.epistimi.parent.ParentFacade
import pl.edu.wat.wcy.epistimi.student.StudentFacade
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class StudentGradeAggregatorService(
    private val gradeRepository: GradeRepository,
    private val gradeAccessValidator: GradeAccessValidator,
    private val classificationGradeRepository: ClassificationGradeRepository,
    private val classificationGradeAccessValidator: ClassificationGradeAccessValidator,
    private val studentFacade: StudentFacade,
    private val parentFacade: ParentFacade,
) {
    /**
     * Retrieves students' grades and classification grades from repository.
     * If requester is STUDENT, it returns this student's grades.
     * If requester is PARENT, it returns grades for all parent's students.
     * If `subjectIds` param was provided, only grades from those subjects will be provided.
     *
     * @param requester User requesting grades information.
     * @param subjectIds Subject ids to be included. If not provided, all subjects grades will be retrieved.
     * @return A summary of students' grades.
     */
    fun getStudentGrades(
        requester: User,
        subjectIds: List<SubjectId>?,
    ): StudentsGrades {
        val students = getContextStudents(requester)
        val studentsGrades = retrieveStudentsGrades(requester, students, subjectIds)
        val studentsClassificationGrades = retrieveStudentsClassificationGrades(requester, students, subjectIds)

        return students
            .map { student ->
                StudentGrades(
                    id = student.id!!,
                    firstName = student.user.firstName,
                    lastName = student.user.lastName,
                    subjects = student.course.subjects
                        .map { subject ->
                            buildSubjectGradesSummary(
                                subject = subject,
                                grades = studentsGrades[student]?.get(subject) ?: emptyList(),
                                classificationGrades = studentsClassificationGrades[student]?.get(subject) ?: emptyList(),
                            )
                        }
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

    private fun retrieveStudentsGrades(
        requester: User,
        students: List<Student>,
        subjectIds: List<SubjectId>?
    ): Map<Student, Map<Subject, List<Grade>>> {
        return students.associateWith { student ->
            retrieveStudentSubjectsGrades(requester, student, subjectIds)
        }
    }

    private fun retrieveStudentSubjectsGrades(
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

    private fun retrieveStudentsClassificationGrades(
        requester: User,
        students: List<Student>,
        subjectIds: List<SubjectId>?
    ): Map<Student, Map<Subject, List<ClassificationGrade>>> {
        return students.associateWith { student ->
            retrieveStudentSubjectsClassificationGrades(requester, student, subjectIds)
        }
    }

    private fun retrieveStudentSubjectsClassificationGrades(
        requester: User,
        student: Student,
        subjectIds: List<SubjectId>?,
    ): Map<Subject, List<ClassificationGrade>> {
        val filters = GradeFilters(
            studentIds = listOf(student.id!!),
            subjectIds = subjectIds,
        )
        return retrieveClassificationGradesWithAuthorizationFiltering(requester, filters)
            .groupBy(ClassificationGrade::subject)
    }

    private fun retrieveClassificationGradesWithAuthorizationFiltering(
        requester: User,
        filters: GradeFilters,
    ): List<ClassificationGrade> {
        return classificationGradeRepository.findAllWithFiltering(filters)
            .filter { grade -> classificationGradeAccessValidator.canRetrieve(requester, grade) }
            .sortedBy(ClassificationGrade::issuedAt)
    }

    private fun buildSubjectGradesSummary(
        subject: Subject,
        grades: List<Grade>,
        classificationGrades
        : List<ClassificationGrade>,
    ): StudentSubjectGradesSummary {
        val allSubjectGrades = retrieveSubjectAllStudentsGrades(subject.id!!)
        val classificationProposal = classificationGrades.firstOrNull { it.isProposal && it.period == SCHOOL_YEAR }
        val classificationFinal = classificationGrades.firstOrNull { !it.isProposal && it.period == SCHOOL_YEAR }

        return StudentSubjectGradesSummary(
            id = subject.id,
            name = subject.name,
            firstSemester = buildStudentSubjectSemesterGradesSummary(
                grades = grades,
                classificationGrades = classificationGrades,
                allSubjectGrades = allSubjectGrades,
                semesterNumber = 1,
            ),
            secondSemester = buildStudentSubjectSemesterGradesSummary(
                grades = grades,
                classificationGrades = classificationGrades,
                allSubjectGrades = allSubjectGrades,
                semesterNumber = 2,
            ),
            average = StudentGradesAverage(
                student = grades.weightedAverage(),
                subject = allSubjectGrades.weightedAverage(),
            ),
            classification = StudentSubjectClassification(
                proposal = classificationProposal,
                final = classificationFinal,
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
        grades: List<Grade>,
        classificationGrades: List<ClassificationGrade>,
        allSubjectGrades: List<Grade>,
        semesterNumber: Int,
    ): StudentSubjectSemesterGradesSummary {
        val studentSemesterGrades = grades.filter { grade -> grade.semester == semesterNumber }
        val allSubjectSemesterGrades = allSubjectGrades.filter { grade -> grade.semester == semesterNumber }

        val classificationPeriod = ClassificationGradePeriod.fromSemesterNumber(semesterNumber)
        val classificationProposal = classificationGrades.firstOrNull { it.isProposal && it.period == classificationPeriod }
        val classificationFinal = classificationGrades.firstOrNull { !it.isProposal && it.period == classificationPeriod }

        return StudentSubjectSemesterGradesSummary(
            grades = studentSemesterGrades,
            average = StudentGradesAverage(
                student = studentSemesterGrades.weightedAverage(),
                subject = allSubjectSemesterGrades.weightedAverage(),
            ),
            classification = StudentSubjectClassification(
                proposal = classificationProposal,
                final = classificationFinal,
            ),
        )
    }
}
