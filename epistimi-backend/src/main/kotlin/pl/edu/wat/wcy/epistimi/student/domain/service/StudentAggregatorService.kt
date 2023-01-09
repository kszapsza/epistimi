package pl.edu.wat.wcy.epistimi.student.domain.service

import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.student.domain.StudentNotFoundException
import pl.edu.wat.wcy.epistimi.student.domain.access.StudentAccessValidator
import pl.edu.wat.wcy.epistimi.student.domain.port.StudentRepository
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId

class StudentAggregatorService(
    private val studentRepository: StudentRepository,
    private val studentAccessValidator: StudentAccessValidator,
) {
    fun getStudent(contextUser: User, studentId: StudentId): Student {
        return studentRepository.findById(studentId)
            .also { student ->
                if (!studentAccessValidator.canRetrieve(contextUser, student)) {
                    throw StudentNotFoundException()
                }
            }
    }

    fun getStudentByUserId(contextUser: User, userId: UserId): Student {
        return studentRepository.findByUserId(userId)
            .also { student ->
                if (!studentAccessValidator.canRetrieve(contextUser, student)) {
                    throw StudentNotFoundException()
                }
            }
    }
}
