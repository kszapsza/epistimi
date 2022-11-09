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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.port.UserRepository
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    @Value("\${epistimi.security.jwt-secret}") private val jwtSecret: String,
) : OncePerRequestFilter() {

    companion object {
        private val BEARER_PATTERN by lazy { Pattern.compile("^Bearer (.+)\$") }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            request.getHeader(HttpHeaders.AUTHORIZATION)
                ?.also { SecurityContextHolder.getContext().authentication = getAuthentication(it) }
        } catch (e: UnauthorizedException) {
            response
                .apply { status = HttpStatus.UNAUTHORIZED.value() }
                .apply { setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_V1) }
                .apply { objectMapper.writeValue(outputStream, ErrorMessage(e, HttpStatus.UNAUTHORIZED, request)) }
        }
        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String): Authentication? {
        return getClaims(authorizationHeader).body
            ?.let { jwtBody -> buildSpringSecurityToken(jwtBody) }
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
            logger.debug("Provided expired authorization token")
            throw UnauthorizedException("Authorization token expired")
        } catch (e: JwtException) {
            logger.debug("Provided malformed authorization token")
            throw UnauthorizedException("Authorization token invalid")
        } catch (e: IllegalArgumentException) {
            logger.debug("Missing authorization token")
            throw UnauthorizedException("Authorization token missing")
        }
    }

    private fun buildSpringSecurityToken(claims: Claims): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(
            /* principal = */ fetchContextUser(claims.subject),
            /* credentials = */ null,
            /* authorities = */ setOf(SimpleGrantedAuthority("ROLE_${claims[JwtClaims.ROLE]}"))
        )
    }

    private fun fetchContextUser(subjectClaim: String): User {
        return try {
            // TODO: disabled organization
            userRepository.findById(UserId(subjectClaim))
        } catch (e: UserNotFoundException) {
            logger.error("Provided token for non-existent user id!: [$subjectClaim]!")
            throw UnauthorizedException("Unauthorized: user with id $subjectClaim not found")
        }
    }
}
