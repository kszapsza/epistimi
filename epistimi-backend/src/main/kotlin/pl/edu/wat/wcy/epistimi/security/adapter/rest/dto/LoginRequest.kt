package pl.edu.wat.wcy.epistimi.security.adapter.rest.dto

data class LoginRequest(
    val username: String,
    val password: String,
)
