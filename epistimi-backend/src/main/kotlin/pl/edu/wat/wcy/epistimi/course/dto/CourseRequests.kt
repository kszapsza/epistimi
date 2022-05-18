package pl.edu.wat.wcy.epistimi.course.dto

import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import java.time.LocalDate
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

data class CourseCreateRequest(
    @field:Min(value = 1, message = "Code number should be >= 1")
    @field:Max(value = 8, message = "Code number should be <= 8")
    val codeNumber: Int,

    @field:Pattern(regexp = "^[a-z]+\$", message = "Only lowercase a-z letters allowed")
    val codeLetter: String,

    @field:FutureOrPresent(message = "School year beginning cannot occur in past")
    val schoolYearBegin: LocalDate,

    @field:FutureOrPresent(message = "School year semester end cannot occur in past")
    val schoolYearSemesterEnd: LocalDate,

    @field:FutureOrPresent(message = "School year end cannot occur in past")
    val schoolYearEnd: LocalDate,

    val classTeacherId: TeacherId,
    val profile: String?,
    val profession: String?,
    val specialization: String?,
)
