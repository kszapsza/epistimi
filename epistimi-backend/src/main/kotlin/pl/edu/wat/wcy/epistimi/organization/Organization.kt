package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

data class Organization(
    val id: OrganizationId? = null,
    val name: String,
    val adminId: UserId,
    val status: Status,
    val directorId: UserId,
    val address: Address,
    val location: Location?,
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

data class OrganizationDetails(
    val id: OrganizationId? = null,
    val name: String,
    val admin: User,
    val status: Organization.Status,
    val director: User,
    val address: Address,
    val location: Location?,
)
