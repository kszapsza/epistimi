package pl.edu.wat.wcy.epistimi.common

data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        fun of(latitude: Double?, longitude: Double?): Location? {
            return if (latitude != null && longitude != null) {
                Location(latitude, longitude)
            } else {
                null
            }
        }
    }
}
