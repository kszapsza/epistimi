package pl.edu.wat.wcy.epistimi.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

class JwtAuthenticationFilterTest : ShouldSpec({

    should("reject a request") {
        forAll(
            row("", "Authorization token missing"),
            row("abc", "Authorization token missing"),
            row("Bearer abc", "Authorization token invalid"),
        ) { authorizationHeader, errorMessage ->
            val objectMapper = ObjectMapper()
            val filterChainMock = MockFilterChain()
            val requestMock = MockHttpServletRequest()
            val responseMock = MockHttpServletResponse()
            val filter = JwtAuthenticationFilter(objectMapper, "123")

            requestMock.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
            filter.doFilter(requestMock, responseMock, filterChainMock)

            responseMock.status shouldBe HttpStatus.UNAUTHORIZED.value()
            objectMapper.readValue(responseMock.contentAsString, Map::class.java)["message"] shouldBe errorMessage
        }
    }

})
