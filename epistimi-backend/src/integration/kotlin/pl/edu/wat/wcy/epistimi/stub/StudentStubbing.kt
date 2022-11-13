package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.student.domain.port.StudentRepository
import pl.edu.wat.wcy.epistimi.user.domain.User

@Component
internal class StudentStubbing(
    private val studentRepository: StudentRepository,
) {
    fun studentExists(
        id: StudentId? = null,
        user: User,
        parents: List<Parent> = emptyList(),
    ): Student {
        return studentRepository.save(
            Student(
                id = id,
                user = user,
                parents = parents,
            )
        )
    }
}
