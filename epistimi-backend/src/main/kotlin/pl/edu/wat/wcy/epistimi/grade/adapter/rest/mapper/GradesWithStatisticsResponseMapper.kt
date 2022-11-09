package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.GradesAverage
import pl.edu.wat.wcy.epistimi.grade.GradesStatistics
import pl.edu.wat.wcy.epistimi.grade.GradesWithStatistics
import pl.edu.wat.wcy.epistimi.grade.StudentGradesWithStatistics
import pl.edu.wat.wcy.epistimi.grade.SubjectGradesWithStatistics
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradesAverageResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradesStatisticsResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradesWithStatisticsResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.StudentGradesWithStatisticsResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.SubjectGradesWithStatisticsResponse

object GradesWithStatisticsResponseMapper :
    FromDomainMapper<GradesWithStatistics, GradesWithStatisticsResponse> {

    override fun fromDomain(domainObject: GradesWithStatistics) =
        domainObject.toGradesWithStatisticsResponse()

    private fun GradesWithStatistics.toGradesWithStatisticsResponse() =
        GradesWithStatisticsResponse(
            subjects = gradesBySubject.map { it.toSubjectGradesWithStatisticsResponse() },
        )

    private fun SubjectGradesWithStatistics.toSubjectGradesWithStatisticsResponse() =
        SubjectGradesWithStatisticsResponse(
            id = subject.id!!.value.toString(),
            name = subject.name,
            students = gradesByStudent.map { it.toStudentGradesWithStatisticsResponse() },
            statistics = statistics.toGradesStatisticsResponse(),
        )

    private fun StudentGradesWithStatistics.toStudentGradesWithStatisticsResponse() =
        StudentGradesWithStatisticsResponse(
            id = student.id!!.value.toString(),
            firstName = student.user.firstName,
            lastName = student.user.lastName,
            grades = grades.map { GradeResponseMapper.fromDomain(it) },
            statistics = statistics.toGradesStatisticsResponse(),
        )

    private fun GradesStatistics.toGradesStatisticsResponse() =
        GradesStatisticsResponse(
            average = average.toGradesAverageResponse(),
        )

    private fun GradesAverage.toGradesAverageResponse() =
        GradesAverageResponse(
            firstSemester = firstSemester?.toString(),
            secondSemester = secondSemester?.toString(),
            overall = overall?.toString(),
        )
}
