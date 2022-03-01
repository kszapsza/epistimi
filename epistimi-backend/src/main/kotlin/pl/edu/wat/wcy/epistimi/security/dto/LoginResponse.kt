package pl.edu.wat.wcy.epistimi.security.dto

data class LoginResponse(
    val token: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val username: String,
)
