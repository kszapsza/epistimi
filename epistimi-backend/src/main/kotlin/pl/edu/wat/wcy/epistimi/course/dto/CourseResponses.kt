package pl.edu.wat.wcy.epistimi.course.dto

import pl.edu.wat.wcy.epistimi.course.CourseDetails
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.student.dto.StudentResponse
import pl.edu.wat.wcy.epistimi.student.dto.toStudentResponse
import pl.edu.wat.wcy.epistimi.teacher.dto.TeacherResponse
import pl.edu.wat.wcy.epistimi.teacher.dto.toTeacherResponse
import java.time.LocalDate

data class CourseResponse(
    val id: CourseId? = null,
    val code: Code,
    val schoolYear: String,
    val classTeacher: TeacherResponse,
    val students: List<StudentResponse>,
    val schoolYearBegin: LocalDate,
    val schoolYearSemesterEnd: LocalDate,
    val schoolYearEnd: LocalDate,
    val profile: String?,
    val profession: String?,
    val specialization: String?,
) {
    data class Code(
        val number: String,
        val letter: String,
    )
}

fun CourseDetails.toCourseResponse() = CourseResponse(
    id = id,
    code = CourseResponse.Code(number = code.number, letter = code.letter),
    schoolYear = schoolYear,
    classTeacher = classTeacher.toTeacherResponse(),
    students = students.map { it.toStudentResponse() },
    schoolYearBegin = schoolYearBegin,
    schoolYearSemesterEnd = schoolYearSemesterEnd,
    schoolYearEnd = schoolYearEnd,
    profile = profile,
    profession = profession,
    specialization = specialization,
)

data class CoursesResponse(
    val courses: List<CourseResponse>,
)
