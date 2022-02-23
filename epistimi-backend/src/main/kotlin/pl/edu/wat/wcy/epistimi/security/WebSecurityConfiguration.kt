package pl.edu.wat.wcy.epistimi.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

@EnableWebSecurity
class WebSecurityConfiguration(
    private val userRepository: UserRepository,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    @Value("\${epistimi.security.password-encoder-strength}") private val passwordEncoderStrength: Int,
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(object : UserDetailsService {
            override fun loadUserByUsername(username: String): UserDetails? {
                return try {
                    userRepository.findByUsername(username).let { user ->
                        User.builder().username(user.username).password(user.passwordHash).build()
                    }
                } catch (e: UserNotFoundException) {
                    throw UsernameNotFoundException("Username \"$username\" was not found")
                }
            }
        })
    }

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()
            ?.csrf()?.disable() // TODO
            ?.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }?.authorizeRequests {
                it.antMatchers(HttpMethod.GET, "/api/article")?.permitAll()
                it.antMatchers(HttpMethod.GET, "/api/article/**")?.permitAll()
//                it.antMatchers(HttpMethod.POST,"/api/user")?.permitAll() // TODO: testing!!!
                it.antMatchers(HttpMethod.POST, "/auth/login")?.permitAll()
                it.anyRequest()?.authenticated()
            }?.exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }?.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/api/**")
                    .allowedMethods(HttpMethod.GET.name)
                    .allowedOrigins("http://localhost:3000")
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(passwordEncoderStrength)
}
