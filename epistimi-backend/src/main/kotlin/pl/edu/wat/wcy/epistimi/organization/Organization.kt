package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location
import pl.edu.wat.wcy.epistimi.user.User
import java.util.UUID

data class Organization(
    val id: OrganizationId? = null,
    val name: String,
    val admin: User,
    val status: Status,
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
    val value: UUID,
)