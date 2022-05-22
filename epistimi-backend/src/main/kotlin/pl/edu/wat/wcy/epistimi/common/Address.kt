package pl.edu.wat.wcy.epistimi.common

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class Address(
    @field:NotBlank
    val street: String,

    @field:NotBlank
    @field:Pattern(regexp = "^\\d{2}-\\d{3}\$")
    val postalCode: String,

    @field:NotBlank
    val city: String,
)
