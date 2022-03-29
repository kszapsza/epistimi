package pl.edu.wat.wcy.epistimi.organization.infrastructure.external

import org.apache.http.client.utils.URIBuilder
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import pl.edu.wat.wcy.epistimi.organization.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location
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
            .apply { path = "search" }
            .apply { addParameter("format", "json") }
            .apply { addParameter("q", "${address.street}, ${address.postalCode} ${address.city}, ${address.countryCode}") }
            .build()
    }

    private fun fetchFromNominatim(url: URI): ResponseEntity<NominatimResponse> {
        return restTemplate.exchange(
            method = HttpMethod.GET,
            url = url,
        )
    }

    private fun getLocation(response: ResponseEntity<NominatimResponse>): Location? {
        return if (response.statusCode != HttpStatus.OK
            || response.body == null
            || response.body!!.isEmpty()
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
        private const val NOMINATIM_ENDPOINT = "https://nominatim.openstreetmap.org/search"
    }
}
