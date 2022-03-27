package pl.edu.wat.wcy.epistimi.organization.infrastructure.external

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import pl.edu.wat.wcy.epistimi.organization.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location

@Component
class NominatimOrganizationLocationClient(
    private val restTemplate: RestTemplate,
) : OrganizationLocationClient {

    override fun getLocation(address: Address): Location? {
        val url: String = buildNominatimUrl(address)
        val response: ResponseEntity<NominatimResponse> = fetchFromNominatim(url)

        return getLocation(response)
    }

    private fun getLocation(response: ResponseEntity<NominatimResponse>): Location? {
        return if (response.statusCode != HttpStatus.OK
            || response.body == null
            || response.body!!.isEmpty()
        ) {
            null
        } else {
            with(response.body!![0]) {
                Location(
                    latitude = lat.toDouble(),
                    longitude = lon.toDouble(),
                )
            }
        }
    }

    private fun fetchFromNominatim(url: String): ResponseEntity<NominatimResponse> {
        return restTemplate.exchange(
            method = HttpMethod.GET,
            url = url,
        )
    }

    private fun buildNominatimUrl(address: Address): String {
        val query = with(address) {
            "$street, $postalCode $city, $countryCode"
        }
        return "$NOMINATIM_ENDPOINT$query"
    }

    companion object {
        private const val NOMINATIM_ENDPOINT: String = "https://nominatim.openstreetmap.org/search?format=json&q=";
    }
}
