package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User

data class Organization(
    val id: OrganizationId? = null,
    val name: String,
    val admin: User,
    val status: Status,
    val director: User,
    val address: Address,
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
