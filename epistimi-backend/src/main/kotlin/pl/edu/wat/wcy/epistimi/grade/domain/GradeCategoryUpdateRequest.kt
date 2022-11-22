package pl.edu.wat.wcy.epistimi.grade.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.edu.wat.wcy.epistimi.common.validation.HexColorCodeOrNull
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
data class GradeCategoryUpdateRequest(

    val categoryId: GradeCategoryId,

    @field:NotBlank
    @field:Size(max = 30)
    val name: String,

    @field:Min(1)
    @field:Max(10)
    val defaultWeight: Int,

    @field:HexColorCodeOrNull
    val color: String?,
)
