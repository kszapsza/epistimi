package pl.edu.wat.wcy.epistimi.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserService
import pl.edu.wat.wcy.epistimi.user.dto.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/{userId}")
    fun getUserById(
        @PathVariable userId: String
    ): ResponseEntity<UserResponse> =
        ResponseEntity.ok(
            userService.getUserById(userId).toResponse()
        )

    @PostMapping
    fun registerUser(
        @RequestBody registerRequest: UserRegisterRequest
    ): ResponseEntity<UserResponse> =
        ResponseEntity.ok(
            userService.registerUser(registerRequest).toResponse()
        )

    private fun User.toResponse() = UserResponse(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        username = this.username,
    )
}
