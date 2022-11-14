package pl.edu.wat.wcy.epistimi.subject.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.user.domain.User

object SubjectResponseMapper : FromDomainMapper<Subject, SubjectResponse> {
    override fun fromDomain(domainObject: Subject) = domainObject.toSubjectResponse()
}

private fun Subject.toSubjectResponse(): SubjectResponse {
    return SubjectResponse(
        id = id!!.value.toString(),
        course = SubjectCourseResponse(
            id = course.id!!.value.toString(),
            code = "${course.codeNumber}${course.codeLetter}",
            schoolYear = "${course.schoolYearBegin.year}/${course.schoolYearEnd.year}",
            classTeacher = SubjectTeacherResponse(
                id = course.classTeacher.id!!.value.toString(),
                academicTitle = course.classTeacher.academicTitle,
                user = course.classTeacher.user.toSubjectUserResponse(),
            ),
            students = course.students.map { student ->
                SubjectStudentResponse(
                    id = student.id!!.value.toString(),
                    user = student.user.toSubjectUserResponse(),
                )
            }.sortedWith(compareBy({ it.user.lastName }, { it.user.firstName }))
        ),
        teacher = teacher.toSubjectTeacherResponse(),
        name = name,
    )
}

private fun User.toSubjectUserResponse(): SubjectUserResponse {
    return SubjectUserResponse(
        id = id!!.value.toString(),
        firstName = firstName,
        lastName = lastName,
        role = role,
        username = username,
    )
}

private fun Teacher.toSubjectTeacherResponse(): SubjectTeacherResponse {
    return SubjectTeacherResponse(
        id = id!!.value.toString(),
        academicTitle = academicTitle,
        user = user.toSubjectUserResponse(),
    )
}
