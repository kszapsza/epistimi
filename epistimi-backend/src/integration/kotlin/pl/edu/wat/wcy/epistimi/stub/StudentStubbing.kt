package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.StudentRepository
import pl.edu.wat.wcy.epistimi.user.User

@Component
internal class StudentStubbing(
    private val studentRepository: StudentRepository,
) {
    fun studentExists(
        id: StudentId? = null,
        user: User,
        organization: Organization,
        parents: List<Parent> = emptyList(),
    ): Student {
        return studentRepository.save(
            Student(
                id = id,
                userId = user.id!!,
                organizationId = organization.id!!,
                parentsIds = parents.map { it.id!! },
            )
        )
    }
}
