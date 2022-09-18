package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar.NewTeacher
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.TeacherRegisterResponse.NewUserResponse
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponseMapper

object TeacherResponseMapper : FromDomainMapper<Teacher, TeacherResponse> {
    override fun fromDomain(domainObject: Teacher) = domainObject.toTeacherResponse()
}

private fun Teacher.toTeacherResponse() = TeacherResponse(
    id = id,
    user = UserResponseMapper.fromDomain(user),
    academicTitle = academicTitle,
)

object TeachersResponseMapper : FromDomainMapper<List<Teacher>, TeachersResponse> {
    override fun fromDomain(domainObject: List<Teacher>): TeachersResponse =
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
