package pl.edu.wat.wcy.epistimi.user.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.user.UserFacade
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
@Tag(name = "user", description = "API for retrieving and managing users in Epistimi system")
class UserController(
    private val userFacade: UserFacade,
) {
    @Operation(
        summary = "Get current user",
        tags = ["user"],
        description = "Returns authenticated user data (\"who am I?\" endpoint)",
    )
    @GetMapping(
        path = ["/current"],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getCurrentUser(
        authentication: Authentication,
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = UserResponseMapper) {
                userFacade.getUserById((authentication.principal as User).id!!)
            }
        )
    }

    @Operation(
        summary = "Get users",
        tags = ["user"],
        description = "Returns all users registered in Epistimi system (query param allows filtering by role)",
    )
    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getUsers(
        @RequestParam(required = false) role: List<UserRole>?,
    ): ResponseEntity<UsersResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = UsersResponseMapper) {
                userFacade.getUsers(role)
            }
        )
    }

    @Operation(
        summary = "Get user by id",
        tags = ["user"],
        description = "Returns a user with provided id",
    )
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
                userFacade.getUserById(UserId(userId))
            }
        )
    }

    @Operation(
        summary = "Register user",
        tags = ["user"],
        description = "Epistimi admin endpoint allowing registering a new user with any role",
    )
    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun registerUser(
        authentication: Authentication,
        @Valid @RequestBody registerRequest: UserRegisterRequest,
    ): ResponseEntity<UserRegisterResponse> {
        return userFacade.registerUser(contextOrganization = (authentication.principal as User).organization, registerRequest)
            .let { (newUser, password) -> UserRegisterResponse(newUser, password) }
            .let { ResponseEntity.created(URI("/api/user/${it.id}")).body(it) }
    }
}
