package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.user.User

data class Organization(
    val id: OrganizationId? = null,
    val name: String,
    val admin: User,
    val status: Status,
) {
    enum class Status {
        ENABLED,
        DISABLED,
    }
}

@JvmInline
value class OrganizationId(
    val value: String,
)
