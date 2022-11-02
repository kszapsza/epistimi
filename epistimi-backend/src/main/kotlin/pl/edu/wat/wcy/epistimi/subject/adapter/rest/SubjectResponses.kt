package pl.edu.wat.wcy.epistimi.subject.adapter.rest

import pl.edu.wat.wcy.epistimi.course.adapter.rest.CourseResponse
import pl.edu.wat.wcy.epistimi.subject.SubjectId
import pl.edu.wat.wcy.epistimi.teacher.adapter.rest.TeacherResponse

data class SubjectResponse(
    val id: SubjectId,
    val course: CourseResponse,
    val teacher: TeacherResponse,
    val name: String,
)
