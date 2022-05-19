package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpMethod.GET
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.organization.adapter.external.NominatimOrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.adapter.external.NominatimResponse
import pl.edu.wat.wcy.epistimi.organization.adapter.external.NominatimResponseEntry
import pl.edu.wat.wcy.epistimi.common.Location
import java.net.URI

internal class NominatimOrganizationLocationClientTest : ShouldSpec({
    val restTemplateMock = mockk<RestTemplate>()
    val locationClient = NominatimOrganizationLocationClient(restTemplateMock)

    should("return null if Nominatim API failed to respond") {
        // given
        every { restTemplateMock.exchange<NominatimResponse>(url = ofType(URI::class), method = GET) }
            .throws(RestClientException(""))

        // when
        val location = locationClient.getLocation(TestData.address)

        // then
        location.shouldBeNull()
    }

    should("return null if Nominatim API returned empty response body") {
        // given
        every { restTemplateMock.exchange<NominatimResponse>(url = ofType(URI::class), method = GET) }
            .returns(ResponseEntity.ok(null))

        // when
        val location = locationClient.getLocation(TestData.address)

        // then
        location.shouldBeNull()
    }

    should("return null if Nominatim API returned empty array") {
        // given
        every { restTemplateMock.exchange<NominatimResponse>(url = ofType(URI::class), method = GET) }
            .returns(ResponseEntity.ok(emptyList()))

        // when
        val location = locationClient.getLocation(TestData.address)

        // then
        location.shouldBeNull()
    }

    should("return Location if Nominatim API returned valid response") {
        // given
        val nominatimResponse = listOf(
            NominatimResponseEntry(
                place_id = 174185670,
                licence = "Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright",
                osm_type = "way",
                osm_id = 301532119,
                boundingbox = listOf(
                    "53.1233387",
                    "53.1234488",
                    "23.0863006",
                    "23.0864887"
                ),
                lat = "53.123393750000005",
                lon = "23.08639463574675",
                display_name = "17, Szkolna, Starosielce, Białystok, województwo podlaskie, 15-640, Polska",
                `class` = "building",
                type = "house",
                importance = 0.33100000000000007,
                icon = null,
            )
        )

        every { restTemplateMock.exchange<NominatimResponse>(url = ofType(URI::class), method = GET) }
            .returns(ResponseEntity.ok(nominatimResponse))

        // when
        val location = locationClient.getLocation(TestData.address)

        // then
        location shouldBe Location(
            latitude = 53.123393750000005,
            longitude = 23.08639463574675,
        )
    }
})
