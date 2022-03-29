package pl.edu.wat.wcy.epistimi.user.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserService
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse
import pl.edu.wat.wcy.epistimi.user.dto.UsersResponse
import pl.edu.wat.wcy.epistimi.user.dto.toUserResponse
import java.net.URI

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {
    @RequestMapping(
        path = ["/current"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getCurrentUser(
        authentication: Authentication,
    ): ResponseEntity<UserResponse> = ResponseEntity.ok(
        userService.getUserByUsername(authentication.principal as String).toUserResponse()
    )

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @RequestMapping(
        path = [""],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getUsers(
        @RequestParam(required = false) role: List<User.Role>?
    ): ResponseEntity<UsersResponse> = ResponseEntity.ok(
        UsersResponse(
            users = userService.getUsers(role).map { it.toUserResponse() }
        )
    )

    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN', 'ORGANIZATION_ADMIN')")
    @RequestMapping(
        path = ["/{userId}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getUserById(
        @PathVariable userId: UserId,
    ): ResponseEntity<UserResponse> = ResponseEntity.ok(
        userService.getUserById(userId).toUserResponse()
    )

    @PreAuthorize("hasAnyRole('EPISTIMI_ADMIN')")
    @RequestMapping(
        path = [""],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun registerUser(
        @RequestBody registerRequest: UserRegisterRequest
    ): ResponseEntity<UserResponse> =
        userService.registerUser(registerRequest).let {
            ResponseEntity
                .created(URI("/api/user/${it.id!!.value}"))
                .body(it.toUserResponse())
        }
}
