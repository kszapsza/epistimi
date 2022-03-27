package pl.edu.wat.wcy.epistimi.organization

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import pl.edu.wat.wcy.epistimi.organization.infrastructure.external.NominatimOrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.infrastructure.external.NominatimResponse
import pl.edu.wat.wcy.epistimi.organization.infrastructure.external.NominatimResponseEntry
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location

internal class NominatimOrganizationLocationClientTest : ShouldSpec({
    val restTemplateMock = mockk<RestTemplate>()
    val locationClient = NominatimOrganizationLocationClient(restTemplateMock)

    should("return null if Nominatim API failed to respond") {
        forAll(
            row(BAD_REQUEST),
            row(NOT_FOUND),
            row(INTERNAL_SERVER_ERROR),
        ) { statusCode ->
            // given
            every { restTemplateMock.exchange<NominatimResponse>(url = ofType(String::class), method = GET) }
                .returns(ResponseEntity(statusCode))

            val address = Address(
                street = "Szkolna 17",
                postalCode = "15-640",
                city = "Białystok",
                countryCode = "PL",
            )

            // when
            val location = locationClient.getLocation(address)

            // then
            location.shouldBeNull()
        }
    }

    should("return null if Nominatim API returned empty response body") {
        // given
        every { restTemplateMock.exchange<NominatimResponse>(url = ofType(String::class), method = GET) }
            .returns(ResponseEntity.ok(null))

        // when
        val address = Address(
            street = "Szkolna 17",
            postalCode = "15-640",
            city = "Białystok",
            countryCode = "PL",
        )

        // when
        val location = locationClient.getLocation(address)

        // then
        location.shouldBeNull()
    }

    should("return null if Nominatim API returned empty array") {
        // given
        every { restTemplateMock.exchange<NominatimResponse>(url = ofType(String::class), method = GET) }
            .returns(ResponseEntity.ok(listOf()))

        // when
        val address = Address(
            street = "Szkolna 17",
            postalCode = "15-640",
            city = "Białystok",
            countryCode = "PL",
        )

        // when
        val location = locationClient.getLocation(address)

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

        every { restTemplateMock.exchange<NominatimResponse>(url = ofType(String::class), method = GET) }
            .returns(ResponseEntity.ok(nominatimResponse))

        // when
        val address = Address(
            street = "Szkolna 17",
            postalCode = "15-640",
            city = "Białystok",
            countryCode = "PL",
        )

        // when
        val location = locationClient.getLocation(address)

        // then
        location shouldBe Location(
            latitude = 53.123393750000005,
            longitude = 23.08639463574675,
        )
    }
})
