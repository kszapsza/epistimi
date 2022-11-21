package pl.edu.wat.wcy.epistimi.subject.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.subject.adapter.rest.dto.CourseSubjectsResponse
import pl.edu.wat.wcy.epistimi.subject.adapter.rest.dto.SchoolYearSubjectsResponse
import pl.edu.wat.wcy.epistimi.subject.adapter.rest.dto.SubjectsEntryResponse
import pl.edu.wat.wcy.epistimi.subject.adapter.rest.dto.SubjectsResponse
import pl.edu.wat.wcy.epistimi.subject.adapter.rest.dto.SubjectsTeacherResponse
import pl.edu.wat.wcy.epistimi.subject.adapter.rest.dto.SubjectsUserResponse
import pl.edu.wat.wcy.epistimi.subject.domain.CourseSubjects
import pl.edu.wat.wcy.epistimi.subject.domain.SchoolYearSubjects
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectsBySchoolYearAndCourse
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.user.domain.User

object SubjectsResponseMapper : FromDomainMapper<SubjectsBySchoolYearAndCourse, SubjectsResponse> {
    override fun fromDomain(domainObject: SubjectsBySchoolYearAndCourse) =
        domainObject.toSubjectsBySchoolYearAndCourseResponse()
}

private fun SubjectsBySchoolYearAndCourse.toSubjectsBySchoolYearAndCourseResponse() =
    SubjectsResponse(
        schoolYears = schoolYears.map { it.toSchoolYearSubjectsResponse() }
    )

private fun SchoolYearSubjects.toSchoolYearSubjectsResponse() =
    SchoolYearSubjectsResponse(
        schoolYear = schoolYear,
        courses = courses.map { it.toCourseSubjectsResponse() }
    )

private fun CourseSubjects.toCourseSubjectsResponse() =
    CourseSubjectsResponse(
        courseId = id.value.toString(),
        code = "$codeNumber$codeLetter",
        classTeacher = classTeacher.toSubjectsTeacherResponse(),
        studentsCount = students.size,
        subjects = subjects.map { it.toSubjectsEntryResponse() }
    )

private fun Teacher.toSubjectsTeacherResponse() =
    SubjectsTeacherResponse(
        id = id!!.value.toString(),
        academicTitle = academicTitle,
        user = user.toSubjectsUserResponse()
    )

private fun User.toSubjectsUserResponse() =
    SubjectsUserResponse(
        id = id!!.value.toString(),
        firstName = firstName,
        lastName = lastName,
        role = role,
        username = username,
    )

private fun Subject.toSubjectsEntryResponse() =
    SubjectsEntryResponse(
        id = id!!.value.toString(),
        teacher = teacher.toSubjectsTeacherResponse(),
        name = name,
    )