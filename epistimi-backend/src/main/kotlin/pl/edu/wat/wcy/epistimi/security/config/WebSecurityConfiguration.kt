package pl.edu.wat.wcy.epistimi.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.edu.wat.wcy.epistimi.security.JwtAuthenticationFilter

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class WebSecurityConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    @Value("\${epistimi.security.password-encoder-strength}") private val passwordEncoderStrength: Int,
    @Value("\${epistimi.security.frontend-app-domain}") private val frontendAppDomain: String,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()
            ?.csrf()?.disable()
            ?.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }?.authorizeRequests {
                it.antMatchers(HttpMethod.GET, "/api/article")?.permitAll()
                it.antMatchers(HttpMethod.GET, "/api/article/**")?.permitAll()
                it.antMatchers(HttpMethod.POST, "/auth/login")?.permitAll()
                it.antMatchers("/swagger-ui/**")?.permitAll()
                it.antMatchers("/swagger-resources/**")?.permitAll()
                it.antMatchers("/v3/api-docs")?.permitAll()
                it.anyRequest()?.authenticated()
            }?.exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }?.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry
                    .addMapping("/api/**")
                    .allowedMethods(
                        HttpMethod.DELETE.name,
                        HttpMethod.GET.name,
                        HttpMethod.POST.name,
                        HttpMethod.PUT.name,
                    )
                    .allowedOrigins(frontendAppDomain)
                registry
                    .addMapping("/auth/**")
                    .allowedMethods(HttpMethod.POST.name)
                    .allowedOrigins(frontendAppDomain)
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(passwordEncoderStrength)
}
