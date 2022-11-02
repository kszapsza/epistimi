package pl.edu.wat.wcy.epistimi.subject.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.course.adapter.rest.CourseResponseMapper
import pl.edu.wat.wcy.epistimi.subject.Subject
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.TeacherResponseMapper

object SubjectResponseMapper : FromDomainMapper<Subject, SubjectResponse> {
    override fun fromDomain(domainObject: Subject) = domainObject.toSubjectResponse()
}

private fun Subject.toSubjectResponse(): SubjectResponse {
    return SubjectResponse(
        id = id!!,
        course = CourseResponseMapper.fromDomain(course),
        teacher = TeacherResponseMapper.fromDomain(teacher),
        name = name,
    )
}
