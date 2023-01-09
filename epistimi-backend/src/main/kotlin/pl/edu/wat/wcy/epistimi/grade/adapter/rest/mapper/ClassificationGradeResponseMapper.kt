package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeCourseResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeStudentResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeSubjectResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeTeacherResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeValueResponse
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeValue
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher

object ClassificationGradeResponseMapper : FromDomainMapper<ClassificationGrade, ClassificationGradeResponse> {
    override fun fromDomain(domainObject: ClassificationGrade): ClassificationGradeResponse {
        return domainObject.toClassificationGradeResponse()
    }
}

private fun ClassificationGrade.toClassificationGradeResponse() =
    ClassificationGradeResponse(
        id = id!!.value.toString(),
        subject = subject.toClassificationGradeSubjectResponse(),
        student = student.toClassificationGradeStudentResponse(),
        issuedBy = issuedBy.toClassificationGradeTeacherResponse(),
        issuedAt = issuedAt!!,
        updatedAt = updatedAt,
        value = value.toClassificationGradeValueResponse(),
        period = period,
        isProposal = isProposal,
    )

private fun Subject.toClassificationGradeSubjectResponse() =
    ClassificationGradeSubjectResponse(
        id = id!!.value.toString(),
        name = name,
        course = course.toClassificationGradeCourseResponse(),
    )

private fun Course.toClassificationGradeCourseResponse() =
    ClassificationGradeCourseResponse(
        id = id!!.value.toString(),
        code = code,
        schoolYear = schoolYear,
    )

private fun Student.toClassificationGradeStudentResponse() =
    ClassificationGradeStudentResponse(
        id = id!!.value.toString(),
        firstName = user.firstName,
        lastName = user.lastName,
    )

private fun Teacher.toClassificationGradeTeacherResponse() =
    ClassificationGradeTeacherResponse(
        id = id!!.value.toString(),
        academicTitle = academicTitle,
        firstName = user.firstName,
        lastName = user.lastName,
    )

private fun GradeValue.toClassificationGradeValueResponse() =
    ClassificationGradeValueResponse(
        displayName = displayName,
        fullName = fullName,
    )
