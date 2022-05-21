package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetails
import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar.NewTeacher
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.TeacherRegisterResponse.NewUserResponse
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponseMapper

object TeacherResponseMapper : FromDomainMapper<TeacherDetails, TeacherResponse> {
    override fun fromDomain(domainObject: TeacherDetails) = domainObject.toTeacherResponse()
}

private fun TeacherDetails.toTeacherResponse() = TeacherResponse(
    id = id,
    user = UserResponseMapper.fromDomain(user),
    academicTitle = academicTitle,
)

object TeachersResponseMapper : FromDomainMapper<List<TeacherDetails>, TeachersResponse> {
    override fun fromDomain(domainObject: List<TeacherDetails>): TeachersResponse =
        TeachersResponse(teachers = domainObject.map { it.toTeacherResponse() })
}

object TeacherRegisterResponseMapper : FromDomainMapper<NewTeacher, TeacherRegisterResponse> {
    override fun fromDomain(domainObject: NewTeacher) = with(domainObject) {
        TeacherRegisterResponse(
            id = id,
            newUser = NewUserResponse(
                user = UserResponseMapper.fromDomain(newUser.user),
                password = newUser.password,
            ),
            academicTitle = academicTitle,
        )
    }
}
