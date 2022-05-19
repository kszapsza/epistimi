package pl.edu.wat.wcy.epistimi.student.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.parent.adapter.rest.toParentResponse
import pl.edu.wat.wcy.epistimi.student.StudentRegistrar.NewStudent
import pl.edu.wat.wcy.epistimi.user.adapter.rest.toUserResponse

object StudentRegisterResponseMapper : FromDomainMapper<NewStudent, StudentRegisterResponse> {
    override fun fromDomain(domainObject: NewStudent) = domainObject.toStudentRegisterResponse()
}

fun NewStudent.toStudentRegisterResponse() = StudentRegisterResponse(
    id = id,
    student = user.let {
        StudentRegisterResponse.NewUserResponse(
            user = it.user.toUserResponse(),
            password = it.password
        )
    },
    parents = parents.map {
        StudentRegisterResponse.NewParentResponse(
            parent = it.parent.toParentResponse(),
            password = it.password
        )
    }
)
