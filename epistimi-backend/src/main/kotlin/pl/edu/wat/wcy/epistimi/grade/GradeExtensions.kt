package pl.edu.wat.wcy.epistimi.grade

fun Collection<Grade>.average(): Double {
    return this
        .mapNotNull { it.value.numericValue }
        .average()
}
