package pl.edu.wat.wcy.epistimi.grade.domain.access

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.security.ResourceAccessValidator
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class ClassificationGradeAccessValidator : ResourceAccessValidator<ClassificationGrade> {
    override fun canCreate(requester: User, resource: ClassificationGrade): Boolean {
        return isRequesterClassTeacherOfGradeCourse(requester, resource) ||
            isRequesterSubjectsTeacher(requester, resource)
    }

    private fun isRequesterSubjectsTeacher(requester: User, grade: ClassificationGrade): Boolean {
        return requester hasRole UserRole.TEACHER &&
            requester.id == grade.subject.teacher.user.id
    }

    private fun isRequesterClassTeacherOfGradeCourse(requester: User, grade: ClassificationGrade): Boolean {
        return requester hasRole UserRole.TEACHER &&
            requester.id == grade.subject.course.classTeacher.user.id
    }
}
