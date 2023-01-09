package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeTeacherResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeValueResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectGradesAverageResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectGradesResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectGradesStudentResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectGradesStudentSemesterResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectStudentClassificationGradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectStudentClassificationResponse
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeValue
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGrades
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGradesAverageSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentClassification
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectStudentSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher

object SubjectGradesResponseMapper : FromDomainMapper<SubjectGrades, SubjectGradesResponse> {
    override fun fromDomain(domainObject: SubjectGrades) =
        domainObject.toSubjectGradesResponse()
}

private fun SubjectGrades.toSubjectGradesResponse() =
    SubjectGradesResponse(
        id = id.value.toString(),
        name = name,
        students = students.map { it.toSubjectGradesStudentResponse() },
        average = average.toSubjectGradesAverageResponse(),
    )

private fun SubjectStudentGradesSummary.toSubjectGradesStudentResponse() =
    SubjectGradesStudentResponse(
        id = id.value.toString(),
        firstName = firstName,
        lastName = lastName,
        firstSemester = firstSemester.toSubjectGradesStudentSemesterResponse(),
        secondSemester = secondSemester.toSubjectGradesStudentSemesterResponse(),
        average = average?.toString(),
        classification = classification.toSubjectStudentClassificationResponse(),
    )

private fun SubjectStudentClassification.toSubjectStudentClassificationResponse() =
    SubjectStudentClassificationResponse(
        proposal = proposal?.toSubjectStudentClassificationGradeResponse(),
        final = final?.toSubjectStudentClassificationGradeResponse(),
    )

private fun ClassificationGrade.toSubjectStudentClassificationGradeResponse() =
    SubjectStudentClassificationGradeResponse(
        id = id!!.value.toString(),
        issuedBy = issuedBy.toClassificationGradeTeacherResponse(),
        issuedAt = issuedAt!!,
        updatedAt = updatedAt,
        value = value.toClassificationGradeValueResponse(),
    )

private fun GradeValue.toClassificationGradeValueResponse() =
    ClassificationGradeValueResponse(displayName, fullName)

private fun Teacher.toClassificationGradeTeacherResponse() =
    ClassificationGradeTeacherResponse(
        id = id!!.value.toString(),
        academicTitle = academicTitle,
        firstName = user.firstName,
        lastName = user.lastName,
    )

private fun SubjectGradesAverageSummary.toSubjectGradesAverageResponse() =
    SubjectGradesAverageResponse(
        firstSemester = firstSemester?.toString(),
        secondSemester = secondSemester?.toString(),
        overall = overall?.toString(),
    )

private fun SubjectStudentSemesterGradesSummary.toSubjectGradesStudentSemesterResponse() =
    SubjectGradesStudentSemesterResponse(
        grades = grades.map { GradeResponseMapper.fromDomain(it) },
        average = average?.toString(),
        classification = classification.toSubjectStudentClassificationResponse(),
    )
