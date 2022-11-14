package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeIssueForbiddenException
import pl.edu.wat.wcy.epistimi.grade.domain.GradeIssueRequest
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.student.StudentFacade
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.user.domain.User

class GradeIssuingService(
    private val gradeRepository: GradeRepository,
    private val gradeAccessValidator: GradeAccessValidator,
    private val gradeCategoryService: GradeCategoryService,
    private val subjectFacade: SubjectFacade,
    private val studentFacade: StudentFacade,
    private val teacherFacade: TeacherFacade,
) {
    fun issueGrade(
        requester: User,
        gradeIssueRequest: GradeIssueRequest,
    ): Grade {
        val grade = buildGrade(requester, gradeIssueRequest)
        if (gradeAccessValidator.canCreate(requester, grade)) {
            return gradeRepository.save(grade)
        }
        throw GradeIssueForbiddenException(gradeIssueRequest.subjectId, gradeIssueRequest.studentId)
    }

    private fun buildGrade(
        requester: User,
        gradeIssueRequest: GradeIssueRequest,
    ): Grade {
        return with(gradeIssueRequest) {
            Grade(
                id = null,
                subject = getGradeSubject(requester, subjectId),
                student = getGradeStudent(requester, studentId),
                issuedBy = getGradeTeacher(requester),
                category = getGradeCategory(requester, categoryId),
                semester = semester,
                issuedAt = null,
                updatedAt = null,
                value = value,
                weight = weight,
                countTowardsAverage = countTowardsAverage,
                comment = comment,
            )
        }
    }

    private fun getGradeSubject(
        requester: User,
        subjectId: SubjectId,
    ): Subject {
        return subjectFacade.getSubject(contextUser = requester, subjectId)
    }

    private fun getGradeStudent(
        requester: User,
        studentId: StudentId,
    ): Student {
        return studentFacade.getStudent(requester, studentId)
    }

    private fun getGradeTeacher(requester: User): Teacher {
        return teacherFacade.getTeacherByUserId(requester, requester.id!!)
    }

    private fun getGradeCategory(
        requester: User,
        categoryId: GradeCategoryId,
    ): GradeCategory {
        return gradeCategoryService.getCategoryById(contextUser = requester, categoryId)
    }
}
