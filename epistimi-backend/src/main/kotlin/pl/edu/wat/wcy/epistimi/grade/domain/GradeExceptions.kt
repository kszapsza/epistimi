package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId

class GradeNotFoundException(id: GradeId) : Exception("Grade with id ${id.value} not found")

class GradeCategoryActionForbiddenException(message: String) : Exception(message)
class GradeCategoryNotFoundException(gradeCategoryId: GradeCategoryId) :
    Exception("Grade category with id [${gradeCategoryId.value}] was not found")

class GradeCategorySubjectNotFoundException(subjectId: SubjectId) : Exception("Subject with id [${subjectId.value}] was not found")