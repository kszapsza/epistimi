package pl.edu.wat.wcy.epistimi.security.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.MediaType
import pl.edu.wat.wcy.epistimi.security.AuthenticationService
import pl.edu.wat.wcy.epistimi.security.dto.LoginRequest
import pl.edu.wat.wcy.epistimi.security.dto.LoginResponse

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
) {
    @RequestMapping(
        path = ["/login"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(
            authenticationService.login(loginRequest)
        )
    }
}
