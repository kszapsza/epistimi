package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetails
import pl.edu.wat.wcy.epistimi.user.adapter.rest.toUserResponse

object TeachersResponseMapper : FromDomainMapper<List<TeacherDetails>, TeachersResponse> {
    override fun fromDomain(domainObject: List<TeacherDetails>): TeachersResponse =
        TeachersResponse(teachers = domainObject.map { it.toTeacherResponse() })
}

fun TeacherDetails.toTeacherResponse() = TeacherResponse(
    id = id,
    user = user.toUserResponse(),
    academicTitle = academicTitle,
)
