package pl.edu.wat.wcy.epistimi.subject.domain

import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import javax.validation.constraints.NotBlank

data class SubjectRegisterRequest(
    val courseId: CourseId,
    val teacherId: TeacherId,
    @field:NotBlank val name: String,
)
