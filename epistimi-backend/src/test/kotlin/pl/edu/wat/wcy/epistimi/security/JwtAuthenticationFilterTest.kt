package pl.edu.wat.wcy.epistimi.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.user.domain.port.UserRepository

internal class JwtAuthenticationFilterTest : ShouldSpec({

    val objectMapper: ObjectMapper = JsonMapper.builder()
        .findAndAddModules()
        .build()

    forAll(
        row("Authorization header missing", "", "Authorization token missing"),
        row("Authorization header without Bearer prefix", "abc", "Authorization token missing"),
        row("Authorization header invalid", "Bearer abc", "Authorization token invalid"),
    ) { scenario, authorizationHeader, errorMessage ->
        should("reject a request ($scenario)") {
            val filterChainMock = MockFilterChain()
            val requestMock = MockHttpServletRequest()
            val responseMock = MockHttpServletResponse()

            val userRepository = mockk<UserRepository>()
            every { userRepository.findByUsername(any()) } returns TestData.Users.student()

            val filter = JwtAuthenticationFilter(objectMapper, userRepository, "123")

            requestMock.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
            filter.doFilter(requestMock, responseMock, filterChainMock)

            responseMock.status shouldBe HttpStatus.UNAUTHORIZED.value()
            objectMapper.readValue(responseMock.contentAsString, Map::class.java)["message"] shouldBe errorMessage
        }
    }
})
