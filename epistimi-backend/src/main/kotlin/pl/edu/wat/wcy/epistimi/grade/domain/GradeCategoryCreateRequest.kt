package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.common.validation.HexColorCodeOrNull
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class GradeCategoryCreateRequest(
    val subjectId: SubjectId,
    @field:NotBlank val name: String,
    @field:Min(1) @field:Max(10) val defaultWeight: Int,
    @field:HexColorCodeOrNull val color: String?,
)
