package pl.edu.wat.wcy.epistimi.student.domain.service

import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.parent.ParentFacade
import pl.edu.wat.wcy.epistimi.parent.domain.service.ParentRegistrationService.NewParent
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.student.domain.StudentRegisterRequest
import pl.edu.wat.wcy.epistimi.student.domain.port.StudentRepository
import pl.edu.wat.wcy.epistimi.user.UserFacade
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService.NewUser

class StudentRegistrationService(
    private val studentRepository: StudentRepository,
    private val userFacade: UserFacade,
    private val parentFacade: ParentFacade,
    private val courseFacade: CourseFacade,
) {
    data class NewStudent(
        val id: StudentId? = null,
        val user: NewUser,
        val parents: List<NewParent>,
    )

    fun registerStudent(
        contextUser: User,
        request: StudentRegisterRequest,
    ): NewStudent {
        val course = courseFacade.getCourse(contextUser, request.courseId)
        val studentUser = registerStudentUser(contextUser.organization!!, request.user)
        val newParents = registerParents(contextUser.organization, request.parents)
        val newStudent = registerStudent(course, studentUser, newParents)

        return NewStudent(
            id = newStudent.id,
            user = studentUser,
            parents = newParents,
        )
    }

    private fun registerStudentUser(
        contextOrganization: Organization,
        userData: UserRegisterRequest,
    ): NewUser {
        return userFacade.registerUser(
            contextOrganization,
            request = userData.copy(role = UserRole.STUDENT),
        )
    }

    private fun registerParents(
        contextOrganization: Organization,
        parentsUserData: List<UserRegisterRequest>,
    ): List<NewParent> {
        return parentFacade.registerParents(
            contextOrganization,
            userRegisterRequests = parentsUserData
        )
    }

    private fun registerStudent(
        course: Course,
        studentUser: NewUser,
        newParents: List<NewParent>,
    ): Student {
        return studentRepository.save(
            Student(
                id = null,
                user = studentUser.user,
                parents = newParents.map { it.parent },
                course = course,
            ),
        )
    }
}
