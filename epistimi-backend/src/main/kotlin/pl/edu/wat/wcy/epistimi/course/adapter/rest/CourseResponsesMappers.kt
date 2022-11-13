package pl.edu.wat.wcy.epistimi.course.adapter.rest

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.parent.adapter.rest.ParentResponseMapper
import pl.edu.wat.wcy.epistimi.student.adapter.rest.StudentResponse
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.TeacherResponseMapper
import pl.edu.wat.wcy.epistimi.user.adapter.rest.UserResponse

object CourseResponseMapper : FromDomainMapper<Course, CourseResponse> {
    override fun fromDomain(domainObject: Course) = domainObject.toCourseResponse()
}

private fun Course.toCourseResponse() = CourseResponse(
    id = id!!.value.toString(),
    code = CourseResponse.Code(number = codeNumber.toString(), letter = codeLetter),
    schoolYear = "${schoolYearBegin.year}/${schoolYearEnd.year}",
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
                    address = Address.of(user.street, user.postalCode, user.city),
                )
            },
            parents = student.parents.map { ParentResponseMapper.fromDomain(it) },
        )
    },
    subjects = subjects.map { it.toCourseSubjectResponse() },
    schoolYearBegin = schoolYearBegin,
    schoolYearSemesterEnd = schoolYearSemesterEnd,
    schoolYearEnd = schoolYearEnd,
    profile = profile,
    profession = profession,
    specialization = specialization,
)

private fun Subject.toCourseSubjectResponse() = CourseSubjectResponse(
    id = id!!.value.toString(),
    name = name,
    teacher = CourseSubjectTeacherResponse(
        id = teacher.id!!.value.toString(),
        academicTitle = teacher.academicTitle,
        firstName = teacher.user.firstName,
        lastName = teacher.user.lastName,
        username = teacher.user.username,
    )
)

object CoursesResponseMapper : FromDomainMapper<List<Course>, CoursesResponse> {
    override fun fromDomain(domainObject: List<Course>) =
        CoursesResponse(courses = domainObject.map { it.toCourseResponse() })
}
