package pl.edu.wat.wcy.epistimi.security

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification
import spock.lang.Unroll

class JwtAuthenticationFilterTest extends Specification {

    private final ObjectMapper objectMapper = new ObjectMapper()
    private final MockFilterChain filterChainMock = new MockFilterChain()
    private final MockHttpServletRequest requestMock = new MockHttpServletRequest()
    private final MockHttpServletResponse responseMock = new MockHttpServletResponse()
    private final String JWT_SECRET = '123'

    @Unroll
    def 'should reject a request #testCase'() {
        given: 'request with wrong Authorization header'
        def filter = new JwtAuthenticationFilter(objectMapper, JWT_SECRET)
        requestMock.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        when: 'filter is applied to the request'
        filter.doFilter(requestMock, responseMock, filterChainMock)

        then: 'response status is HTTP 401'
        responseMock.status == HttpStatus.UNAUTHORIZED.value()

        and: 'proper error message is returned in response body'
        def error = new JsonSlurper().parseText(responseMock.contentAsString)
        error['message'] == errorMessage

        where:
        authorizationHeader | testCase                                          || errorMessage
        ''                  | 'with empty Authorization header'                 || 'Authorization token missing'
        'abc'               | 'with Authorization header lacking Bearer prefix' || 'Authorization token missing'
        'Bearer abc'        | 'with invalid Authorization header'               || 'Authorization token invalid'
    }
}