package pl.edu.wat.wcy.epistimi.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pl.edu.wat.wcy.epistimi.shared.api.ErrorMessage
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    private val objectMapper: ObjectMapper,
    @Value("\${epistimi.security.jwt-secret}") private val jwtSecret: String,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            request.getHeader(HttpHeaders.AUTHORIZATION)
                ?.also { SecurityContextHolder.getContext().authentication = getAuthentication(it) }
        } catch (e: UnauthorizedException) {
            response
                .apply { status = HttpStatus.UNAUTHORIZED.value() }
                .apply { setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) }
                .apply { objectMapper.writeValue(outputStream, ErrorMessage(e, HttpStatus.UNAUTHORIZED, request)) }
        }
        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String): Authentication? {
        return getClaims(authorizationHeader).body
            ?.let { jwtBody ->
                UsernamePasswordAuthenticationToken(
                    jwtBody.subject,
                    null,
                    setOf(SimpleGrantedAuthority("ROLE_${jwtBody[JwtClaims.ROLE]}"))
                )
            }
    }

    private fun getClaims(authorizationHeader: String): Jws<Claims> {
        return authorizationHeader
            .let { header -> BEARER_PATTERN.matcher(header) }
            .let { bearerMatcher -> if (bearerMatcher.matches()) bearerMatcher.group(1).trim() else "" }
            .let { bearerToken -> decodeToken(bearerToken) }
    }

    private fun decodeToken(token: String): Jws<Claims> {
        return try {
            Jwts.parser()
                .setSigningKey(jwtSecret.toByteArray())
                .parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw UnauthorizedException("Authorization token expired")
        } catch (e: JwtException) {
            throw UnauthorizedException("Authorization token invalid")
        } catch (e: IllegalArgumentException) {
            throw UnauthorizedException("Authorization token missing")
        }
    }

    companion object {
        private val BEARER_PATTERN = Pattern.compile("^Bearer (.+)\$")!!
    }
}
