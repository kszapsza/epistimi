package pl.edu.wat.wcy.epistimi.student

import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.student.domain.StudentRegisterRequest
import pl.edu.wat.wcy.epistimi.student.domain.service.StudentAggregatorService
import pl.edu.wat.wcy.epistimi.student.domain.service.StudentRegistrationService
import pl.edu.wat.wcy.epistimi.user.domain.User

class StudentFacade(
    private val studentAggregatorService: StudentAggregatorService,
    private val studentRegistrationService: StudentRegistrationService,
) {
    fun getStudent(
        contextUser: User,
        studentId: StudentId,
    ): Student {
        return studentAggregatorService.getStudent(contextUser, studentId)
    }

    fun registerStudent(
        contextUser: User,
        request: StudentRegisterRequest,
    ): StudentRegistrationService.NewStudent {
        return studentRegistrationService.registerStudent(contextUser, request)
    }
}
