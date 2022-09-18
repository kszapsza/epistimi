package pl.edu.wat.wcy.epistimi.course.adapter.rest

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.parent.adapter.rest.ParentResponseMapper
import pl.edu.wat.wcy.epistimi.student.adapter.rest.StudentResponse
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.TeacherResponseMapper
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

object CourseResponseMapper : FromDomainMapper<Course, CourseResponse> {
    override fun fromDomain(domainObject: Course) = domainObject.toCourseResponse()
}

private fun Course.toCourseResponse() = CourseResponse(
    id = id,
    code = CourseResponse.Code(number = code.number.toString(), letter = code.letter),
    schoolYear = schoolYear,
    classTeacher = TeacherResponseMapper.fromDomain(classTeacher),
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
            parents = student.parents.map { ParentResponseMapper.fromDomain(it) },
        )
    },
    schoolYearBegin = schoolYearBegin,
    schoolYearSemesterEnd = schoolYearSemesterEnd,
    schoolYearEnd = schoolYearEnd,
    profile = profile,
    profession = profession,
    specialization = specialization,
)

object CoursesResponseMapper : FromDomainMapper<List<Course>, CoursesResponse> {
    override fun fromDomain(domainObject: List<Course>) =
        CoursesResponse(courses = domainObject.map { it.toCourseResponse() })
}
