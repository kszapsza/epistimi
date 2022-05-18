package pl.edu.wat.wcy.epistimi.user.adapter.rest

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.shared.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserAggregator
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userAggregator: UserAggregator,
    private val userRegistrar: UserRegistrar,
) {
    @GetMapping(
        path = ["/current"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getCurrentUser(
        authentication: Authentication,
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = UserResponseMapper) {
                userAggregator.getUserById(UserId(authentication.principal as String))
            }
        )
    }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getUsers(
        @RequestParam(required = false) role: List<User.Role>?,
    ): ResponseEntity<UsersResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = UsersResponseMapper) {
                userAggregator.getUsers(role)
            }
        )
    }

    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN', 'ORGANIZATION_ADMIN')")
    @GetMapping(
        path = ["/{userId}"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getUserById(
        @PathVariable userId: String,
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = UserResponseMapper) {
                userAggregator.getUserById(UserId(userId))
            }
        )
    }

    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun registerUser(
        @Valid @RequestBody registerRequest: UserRegisterRequest,
    ): ResponseEntity<UserRegisterResponse> {
        return userRegistrar.registerUser(registerRequest)
            .let { (newUser, password) -> UserRegisterResponse(newUser, password) }
            .let { ResponseEntity.created(URI("/api/user/${it.id}")).body(it) }
    }
}
