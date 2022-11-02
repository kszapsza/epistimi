package pl.edu.wat.wcy.epistimi.subject

import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import javax.validation.constraints.NotBlank

data class SubjectRegisterRequest(
    val courseId: CourseId,
    val teacherId: TeacherId,
    @field:NotBlank val name: String,
)
