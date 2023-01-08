package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeTeacherResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.ClassificationGradeValueResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesAverageResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesSubjectResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesSubjectSemesterResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentSubjectClassificationGradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentSubjectClassificationResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentsGradesResponse
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeValue
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGrades
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGradesAverage
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectClassification
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentsGrades
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher

object StudentGradesResponseMapper : FromDomainMapper<StudentsGrades, StudentsGradesResponse> {
    override fun fromDomain(domainObject: StudentsGrades) =
        domainObject.toStudentsGradesResponse()
}

private fun StudentsGrades.toStudentsGradesResponse() =
    StudentsGradesResponse(
        students = students.map { it.toStudentGradesResponse() }
    )

private fun StudentGrades.toStudentGradesResponse() =
    StudentGradesResponse(
        id = id.value.toString(),
        firstName = firstName,
        lastName = lastName,
        subjects = subjects.map { it.toStudentGradesSubjectResponse() },
    )

private fun StudentSubjectGradesSummary.toStudentGradesSubjectResponse() =
    StudentGradesSubjectResponse(
        id = id.value.toString(),
        name = name,
        firstSemester = firstSemester.toStudentGradesSubjectSemesterResponse(),
        secondSemester = secondSemester.toStudentGradesSubjectSemesterResponse(),
        average = average.toStudentGradesAverageResponse(),
        classification = classification.toStudentSubjectClassificationResponse(),
    )

private fun StudentSubjectClassification.toStudentSubjectClassificationResponse() =
    StudentSubjectClassificationResponse(
        proposal = proposal?.toStudentSubjectClassificationGradeResponse(),
        final = final?.toStudentSubjectClassificationGradeResponse(),
    )

private fun ClassificationGrade.toStudentSubjectClassificationGradeResponse() =
    StudentSubjectClassificationGradeResponse(
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

private fun StudentSubjectSemesterGradesSummary.toStudentGradesSubjectSemesterResponse() =
    StudentGradesSubjectSemesterResponse(
        grades = grades.map { GradeResponseMapper.fromDomain(it) },
        average = average.toStudentGradesAverageResponse(),
        classification = classification.toStudentSubjectClassificationResponse(),
    )

private fun StudentGradesAverage.toStudentGradesAverageResponse() =
    StudentGradesAverageResponse(
        student = student?.toString(),
        subject = subject?.toString(),
    )
