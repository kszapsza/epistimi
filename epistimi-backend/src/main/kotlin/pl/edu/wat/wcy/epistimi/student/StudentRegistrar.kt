package pl.edu.wat.wcy.epistimi.student

import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.course.CourseService
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar.NewParent
import pl.edu.wat.wcy.epistimi.student.dto.StudentRegisterRequest
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.UserRegistrar.NewUser
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest

@Service
class StudentRegistrar(
    private val studentRepository: StudentRepository,
    private val userRegistrar: UserRegistrar,
    private val parentRegistrar: ParentRegistrar,
    private val courseService: CourseService,
    private val organizationContextProvider: OrganizationContextProvider,
) {
    data class NewStudent(
        val id: StudentId? = null,
        val user: NewUser,
        val parents: List<NewParent>,
    )

    fun registerStudent(
        requesterUserId: UserId,
        request: StudentRegisterRequest,
    ): NewStudent {
        val organization = organizationContextProvider.provide(requesterUserId)
            ?: throw StudentBadRequestException("User not managing any organization")

        // TODO: very poor performance, response times even over 1500 ms (for student+2 parents = 6 DB insertions)
        //  we have to consider bulking inserts:
        //      (1) 3x users
        //      (2) 2x parents
        //      (3) 1x student
        //  TOTAL: 3 instead of 6 inserts

        val studentUser = registerStudentUser(request.user)
        val newParents = registerParents(requesterUserId, request.parents)
        val newStudent = registerStudent(studentUser, organization, newParents)

        courseService.addStudent(
            courseId = request.courseId,
            student = newStudent,
        )

        return NewStudent(
            id = newStudent.id,
            user = studentUser,
            parents = newParents,
        )
    }

    private fun registerStudentUser(userData: UserRegisterRequest): NewUser {
        return userRegistrar.registerUser(
            userData.copy(role = User.Role.STUDENT)
        )
    }

    private fun registerParents(
        requesterUserId: UserId,
        parentsUserData: List<UserRegisterRequest>,
    ): List<NewParent> {
        return parentsUserData
            .map { userData -> parentRegistrar.registerParent(requesterUserId, userData) }
    }

    private fun registerStudent(
        studentUser: NewUser,
        organization: Organization,
        newParents: List<NewParent>,
    ): Student {
        return studentRepository.save(
            Student(
                id = null,
                user = studentUser.user,
                organization = organization,
                parents = newParents.map { it.parent }
            )
        )
    }

}