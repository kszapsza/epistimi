package pl.edu.wat.wcy.epistimi.grade.domain.access

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.security.ResourceAccessValidator
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole

class GradeAccessValidator : ResourceAccessValidator<Grade> {
    override fun canRetrieve(requester: User, resource: Grade): Boolean {
        return resource isIssuedBy requester || resource isIssuedFor requester ||
                isRequesterParentOfGradeStudent(requester, resource) ||
                isRequesterSubjectsTeacher(requester, resource) ||
                isRequesterClassTeacherOfGradeCourse(requester, resource) ||
                isRequesterAdminOfGradeOrganization(requester, resource)
    }

    private fun isRequesterParentOfGradeStudent(requester: User, grade: Grade): Boolean {
        return requester hasRole UserRole.PARENT &&
                requester.id in grade.student.parents.map { it.user.id }
    }

    private fun isRequesterSubjectsTeacher(requester: User, grade: Grade): Boolean {
        return requester hasRole UserRole.TEACHER &&
                requester.id == grade.subject.teacher.user.id
    }

    private fun isRequesterClassTeacherOfGradeCourse(requester: User, grade: Grade): Boolean {
        return requester hasRole UserRole.TEACHER &&
                requester.id == grade.subject.course.classTeacher.user.id
    }

    private fun isRequesterAdminOfGradeOrganization(requester: User, grade: Grade): Boolean {
        return requester hasRole UserRole.ORGANIZATION_ADMIN &&
                requester.organization!!.id == grade.subject.course.organization.id
    }

    override fun canCreate(requester: User, resource: Grade): Boolean {
        return isRequesterClassTeacherOfGradeCourse(requester, resource) ||
                isRequesterSubjectsTeacher(requester, resource)
    }
}
