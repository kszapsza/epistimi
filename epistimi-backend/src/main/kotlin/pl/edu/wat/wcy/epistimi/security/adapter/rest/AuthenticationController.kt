package pl.edu.wat.wcy.epistimi.security.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.security.AuthenticationService
import pl.edu.wat.wcy.epistimi.security.adapter.rest.dto.LoginRequest
import pl.edu.wat.wcy.epistimi.security.adapter.rest.dto.LoginResponse

@RestController
@RequestMapping("/auth")
@Tag(name = "authentication", description = "API for issuing token for provided user credentials")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
) {
    @Operation(
        summary = "Login endpoint",
        tags = ["authentication"],
        description = "Issues a JWT token for provided username and password",
    )
    @PostMapping(
        path = ["/login"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(
            authenticationService.login(loginRequest)
        )
    }
}
