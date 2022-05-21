package pl.edu.wat.wcy.epistimi.student.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.parent.adapter.rest.ParentResponseMapper
import pl.edu.wat.wcy.epistimi.student.StudentRegistrar.NewStudent
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponseMapper

object StudentRegisterResponseMapper : FromDomainMapper<NewStudent, StudentRegisterResponse> {
    override fun fromDomain(domainObject: NewStudent) = domainObject.toStudentRegisterResponse()
}

private fun NewStudent.toStudentRegisterResponse() = StudentRegisterResponse(
    id = id,
    student = user.let {
        StudentRegisterResponse.NewUserResponse(
            user = UserResponseMapper.fromDomain(it.user),
            password = it.password
        )
    },
    parents = parents.map {
        StudentRegisterResponse.NewParentResponse(
            parent = ParentResponseMapper.fromDomain(it.parent),
            password = it.password
        )
    }
)
