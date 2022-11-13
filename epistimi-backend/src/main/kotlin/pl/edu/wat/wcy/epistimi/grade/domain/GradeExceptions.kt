package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId

class GradeIssueForbiddenException(subjectId: SubjectId, studentId: StudentId) :
    Exception("User not allowed to issue grade (subjectId=${subjectId.value}, studentId=${studentId.value})")

class GradeNotFoundException(id: GradeId) :
    Exception("Grade with id ${id.value} not found")

class GradeCategoryActionForbiddenException(message: String) :
    Exception(message)

class GradeCategoryNotFoundException(categoryId: GradeCategoryId) :
    Exception("Grade category with id [${categoryId.value}] was not found")

class GradeCategorySubjectNotFoundException(subjectId: SubjectId) :
    Exception("Subject with id [${subjectId.value}] was not found")