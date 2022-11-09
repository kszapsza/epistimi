package pl.edu.wat.wcy.epistimi.grade

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Evaluates grades weighted average (mean) value. Grades marked as not counted towards
 * average and grades without numeric values (np, nb, nz etc.) are not taken
 * into account. If empty collection was provided, `null` value is returned.
 *
 * @return weighted average [BigDecimal] object, `null` in case of empty grades collection
 */
fun Collection<Grade>.weightedAverage(): BigDecimal? {
    if (this.isEmpty()) {
        return null
    }
    return this
        .filter { it.countTowardsAverage && it.value.numericValue != null }
        .map { WeightedValue(value = it.value.numericValue!!, weight = it.weight) }
        .let { calculateWeightedAverage(it) }
}

private data class WeightedValue(
    val value: BigDecimal,
    val weight: BigDecimal,
) {
    constructor(value: Int, weight: Int) : this(
        value = BigDecimal(value),
        weight = BigDecimal(weight),
    )
}

private fun calculateWeightedAverage(values: Collection<WeightedValue>): BigDecimal {
    val numerator = values.sumOf { it.weight * it.value }.setScale(2, RoundingMode.HALF_UP)
    val denominator = values.sumOf { it.weight }.setScale(2, RoundingMode.HALF_UP)

    return numerator / denominator
}
