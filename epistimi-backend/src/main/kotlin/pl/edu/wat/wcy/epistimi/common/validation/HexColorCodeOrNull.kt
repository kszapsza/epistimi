package pl.edu.wat.wcy.epistimi.common.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * The annotated [String] must be a valid hexadecimal color code,
 * such as `#ff0000`, or `#f00` or `null`.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [HexColorCodeOrNullValidator::class])
annotation class HexColorCodeOrNull(
    val message: String = "Invalid hexadecimal color code",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class HexColorCodeOrNullValidator : ConstraintValidator<HexColorCodeOrNull, String> {
    private companion object {
        private val HEXADECIMAL_COLOR_CODE_REGEX by lazy { Regex("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$") }
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {
        return value == null || value.matches(HEXADECIMAL_COLOR_CODE_REGEX)
    }
}
