package pl.edu.wat.wcy.epistimi.course.dto

import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationResponse
import pl.edu.wat.wcy.epistimi.organization.dto.toOrganizationResponse
import pl.edu.wat.wcy.epistimi.student.dto.StudentResponse
import pl.edu.wat.wcy.epistimi.student.dto.toStudentResponse
import pl.edu.wat.wcy.epistimi.teacher.dto.TeacherResponse
import pl.edu.wat.wcy.epistimi.teacher.dto.toTeacherResponse

data class CourseResponse(
    val id: CourseId? = null,
    val organization: OrganizationResponse,
    val code: Code,
    val schoolYear: String,
    val classTeacher: TeacherResponse,
    val students: List<StudentResponse>,
) {
    data class Code(
        val number: Int,
        val letter: String,
    )
}

fun Course.toCourseResponse() = CourseResponse(
    id = id,
    organization = organization.toOrganizationResponse(),
    code = CourseResponse.Code(number = code.number, letter = code.letter),
    schoolYear = schoolYear,
    classTeacher = classTeacher.toTeacherResponse(),
    students = students.map { it.toStudentResponse() },
)

data class CoursesResponse(
    val courses: List<CourseResponse>,
)
