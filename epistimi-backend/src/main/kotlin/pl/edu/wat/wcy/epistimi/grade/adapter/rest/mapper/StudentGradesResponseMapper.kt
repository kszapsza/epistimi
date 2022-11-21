package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesAverageResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesSubjectResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesSubjectSemesterResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentsGradesResponse
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGrades
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGradesAverage
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentSubjectSemesterGradesSummary
import pl.edu.wat.wcy.epistimi.grade.domain.StudentsGrades

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
    )

private fun StudentSubjectSemesterGradesSummary.toStudentGradesSubjectSemesterResponse() =
    StudentGradesSubjectSemesterResponse(
        grades = grades.map { GradeResponseMapper.fromDomain(it) },
        average = average.toStudentGradesAverageResponse(),
    )

private fun StudentGradesAverage.toStudentGradesAverageResponse() =
    StudentGradesAverageResponse(
        student = student?.toString(),
        subject = student?.toString(),
    )