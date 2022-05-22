package pl.edu.wat.wcy.epistimi.organization.adapter.external

import org.apache.http.client.utils.URIBuilder
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import java.net.URI

@Component
class NominatimOrganizationLocationClient(
    private val restTemplate: RestTemplate,
) : OrganizationLocationClient {

    override fun getLocation(address: Address): Location? {
        return try {
            val url = buildNominatimUrl(address)
            val response = fetchFromNominatim(url)
            getLocation(response)
        } catch (e: RestClientException) {
            null
        }
    }

    private fun buildNominatimUrl(address: Address): URI {
        return URIBuilder(NOMINATIM_ENDPOINT)
            .setPath("search")
            .addParameter("format", "json")
            .addParameter("q", buildQuery(address))
            .build()
    }

    private fun buildQuery(address: Address): String {
        return "${address.street}, ${address.postalCode} ${address.city}, PL"
    }

    private fun fetchFromNominatim(url: URI): ResponseEntity<NominatimResponse> {
        return restTemplate.exchange(method = HttpMethod.GET, url = url)
    }

    private fun getLocation(response: ResponseEntity<NominatimResponse>): Location? {
        return if (response.statusCode != HttpStatus.OK ||
            response.body == null ||
            response.body!!.isEmpty()
        ) {
            null
        } else {
            response.body!![0].toLocation()
        }
    }

    private fun NominatimResponseEntry.toLocation() = Location(
        latitude = lat.toDouble(),
        longitude = lon.toDouble(),
    )

    companion object {
        private const val NOMINATIM_ENDPOINT = "https://nominatim.openstreetmap.org/search" // TODO: move to application.yml, use @Value
    }
}
