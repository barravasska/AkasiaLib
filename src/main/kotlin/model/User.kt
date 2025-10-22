package model

data class User(
    val id: String,
    val username: String,
    val password: String,
    val role: Role
)