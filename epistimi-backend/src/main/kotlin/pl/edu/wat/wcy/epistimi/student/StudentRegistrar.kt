package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar.NewParent
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import pl.edu.wat.wcy.epistimi.user.UserRole

class StudentRegistrar(
    private val studentRepository: StudentRepository,
    private val userRegistrar: UserRegistrar,
    private val parentRegistrar: ParentRegistrar,
    private val courseFacade: CourseFacade,
) {
    data class NewStudent(
        val id: StudentId? = null,
        val user: NewUser,
        val parents: List<NewParent>,
    )

    fun registerStudent(
        contextOrganization: Organization,
        request: StudentRegisterRequest,
    ): NewStudent {
        val studentUser = registerStudentUser(contextOrganization, request.user)
        val newParents = registerParents(contextOrganization, request.parents)
        val newStudent = registerStudent(studentUser, newParents)

        courseFacade.addStudent(
            courseId = request.courseId,
            studentId = newStudent.id!!,
        )

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
        return userRegistrar.registerUser(
            contextOrganization,
            request = userData.copy(role = UserRole.STUDENT),
        )
    }

    private fun registerParents(
        contextOrganization: Organization,
        parentsUserData: List<UserRegisterRequest>,
    ): List<NewParent> {
        return parentRegistrar.registerParents(contextOrganization, parentsUserData)
    }

    private fun registerStudent(
        studentUser: NewUser,
        newParents: List<NewParent>,
    ): Student {
        return studentRepository.save(
            Student(
                id = null,
                user = studentUser.user,
                parents = newParents.map { it.parent },
            ),
        )
    }
}
