package pl.edu.wat.wcy.epistimi.course.adapter.rest

import pl.edu.wat.wcy.epistimi.course.CourseDetails
import pl.edu.wat.wcy.epistimi.parent.adapter.rest.toParentResponse
import pl.edu.wat.wcy.epistimi.shared.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.student.adapter.rest.StudentResponse
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.toTeacherResponse
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

object CourseResponseMapper : FromDomainMapper<CourseDetails, CourseResponse> {
    override fun fromDomain(domainObject: CourseDetails) = domainObject.toCourseResponse()
}

object CoursesResponseMapper : FromDomainMapper<List<CourseDetails>, CoursesResponse> {
    override fun fromDomain(domainObject: List<CourseDetails>) =
        CoursesResponse(courses = domainObject.map { it.toCourseResponse() })
}

private fun CourseDetails.toCourseResponse() = CourseResponse(
    id = id,
    code = CourseResponse.Code(number = code.number, letter = code.letter),
    schoolYear = schoolYear,
    classTeacher = classTeacher.toTeacherResponse(),
    students = students.map { student ->
        StudentResponse(
            id = student.id,
            user = student.user.let { user ->
                UserResponse(
                    id = user.id!!.value,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    role = user.role,
                    username = user.username,
                    pesel = user.pesel,
                    sex = user.sex,
                    email = user.email,
                    phoneNumber = user.phoneNumber,
                    address = user.address,
                )
            },
            parents = student.parents.map { it.toParentResponse() },
        )
    },
    schoolYearBegin = schoolYearBegin,
    schoolYearSemesterEnd = schoolYearSemesterEnd,
    schoolYearEnd = schoolYearEnd,
    profile = profile,
    profession = profession,
    specialization = specialization,
)
