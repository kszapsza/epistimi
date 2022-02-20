package pl.edu.wat.wcy.epistimi.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    @Value("\${epistimi.security.jwt-secret}") private val jwtSecret: String,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            SecurityContextHolder.getContext().authentication = getAuthentication(request)
        } catch (e: UnauthorizedException) {
            response.status = HttpStatus.UNAUTHORIZED.value()
        } finally {
            filterChain.doFilter(request, response)
        }
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        return getClaims(request)?.body
            ?.let { jwtBody ->
                UsernamePasswordAuthenticationToken(
                    jwtBody.subject,
                    null,
                    setOf(SimpleGrantedAuthority(jwtBody[JwtClaims.ROLE] as String?))
                )
            }
    }

    private fun getClaims(request: HttpServletRequest): Jws<Claims>? {
        return request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.let { authorizationHeader -> BEARER_PATTERN.matcher(authorizationHeader) }
            ?.let { bearerMatcher -> if (bearerMatcher.matches()) bearerMatcher.group(1).trim() else null }
            ?.let { bearerToken -> decodeToken(bearerToken) }
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
        }
    }

    companion object {
        private val BEARER_PATTERN = Pattern.compile("^Bearer (.+)\$")!!
    }
}
