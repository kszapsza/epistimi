package pl.edu.wat.wcy.epistimi.course.adapter.rest

import pl.edu.wat.wcy.epistimi.student.adapter.rest.StudentResponse
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.TeacherResponse
import java.time.LocalDate

data class CourseResponse(
    val id: String,
    val code: Code,
    val schoolYear: String,
    val classTeacher: TeacherResponse,
    val students: List<StudentResponse>,
    val subjects: List<CourseSubjectResponse>,
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

data class CourseSubjectResponse(
    val id: String,
    val teacher: CourseSubjectTeacherResponse,
    val name: String,
)

data class CourseSubjectTeacherResponse(
    val id: String,
    val academicTitle: String?,
    val firstName: String,
    val lastName: String,
    val username: String,
)

data class CoursesResponse(
    val courses: List<CourseResponse>,
)
