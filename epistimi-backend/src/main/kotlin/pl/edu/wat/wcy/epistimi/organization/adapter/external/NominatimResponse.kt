package pl.edu.wat.wcy.epistimi.organization.adapter.external

import com.fasterxml.jackson.annotation.JsonProperty

typealias NominatimResponse = List<NominatimResponseEntry>

data class NominatimResponseEntry(
    @get:JsonProperty("place_id")
    val placeId: Long,

    val licence: String,

    @get:JsonProperty("osm_type")
    val osmType: String,

    @get:JsonProperty("osm_id")
    val osmId: Long,

    @get:JsonProperty("boundingbox")
    val boundingBox: List<String>,

    val lat: String,
    val lon: String,

    @get:JsonProperty("display_name")
    val displayName: String,

    val `class`: String,
    val type: String,
    val importance: Double,
    val icon: String?,
)
