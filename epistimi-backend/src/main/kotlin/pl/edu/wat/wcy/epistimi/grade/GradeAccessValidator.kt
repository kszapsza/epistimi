package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserRole

object GradeAccessValidator {

    /**
     * @return `true`, if user should have access to requested grade.
     */
    fun validateGradeAccess(user: User, grade: Grade): Boolean {
        return grade isIssuedBy user || grade isIssuedFor user ||
            isRequesterParentOfGradeStudent(user, grade) ||
            isRequesterClassTeacherOfGradeCourse(user, grade) ||
            isRequesterAdminOfGradeOrganization(user, grade)
    }

    /**
     * @return `true`, if user is a parent of a student, which received requested grade.
     */
    private fun isRequesterParentOfGradeStudent(requester: User, grade: Grade): Boolean {
        return requester hasRole UserRole.PARENT &&
            requester.id in grade.student.parents.map { it.user.id }
    }

    /**
     * @return `true`, if user is a class teacher of requested grade's context course.
     */
    private fun isRequesterClassTeacherOfGradeCourse(requester: User, grade: Grade): Boolean {
        return requester hasRole UserRole.TEACHER &&
            requester.id == grade.subject.course.classTeacher.user.id
    }

    /**
     * @return `true`, if user is an admin of this grade's context organization.
     */
    private fun isRequesterAdminOfGradeOrganization(requester: User, grade: Grade): Boolean {
        return requester hasRole UserRole.ORGANIZATION_ADMIN &&
            requester.organization!!.id == grade.subject.course.organization.id
    }
}
