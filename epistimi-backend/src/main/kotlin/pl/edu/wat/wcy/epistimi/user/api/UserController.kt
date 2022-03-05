package pl.edu.wat.wcy.epistimi.user.api

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserService
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {
    @RequestMapping(
        path = ["/current"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getCurrentUser(
        authentication: Authentication
    ): ResponseEntity<UserResponse> = ResponseEntity.ok(
        userService.getUserByUsername(authentication.principal as String).toResponse()
    )

    @RequestMapping(
        path = ["/{userId}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getUserById(
        @PathVariable userId: String
    ): ResponseEntity<UserResponse> = ResponseEntity.ok(
        userService.getUserById(userId).toResponse()
    )

    private fun User.toResponse() = UserResponse(
        id = this.id!!.value,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        username = this.username,
    )
}
