package pl.edu.wat.wcy.epistimi.organization.adapter.external

typealias NominatimResponse = List<NominatimResponseEntry>

data class NominatimResponseEntry(
    val place_id: Long,
    val licence: String,
    val osm_type: String,
    val osm_id: Long,
    val boundingbox: List<String>,
    val lat: String,
    val lon: String,
    val display_name: String,
    val `class`: String,
    val type: String,
    val importance: Double,
    val icon: String?,
)
