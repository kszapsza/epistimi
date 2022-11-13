package pl.edu.wat.wcy.epistimi.organization.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Where
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "organizations")
class Organization(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: OrganizationId? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: OrganizationStatus,

    @OneToMany(mappedBy = "organization")
    @Where(clause = "role = 'ORGANIZATION_ADMIN'")
    val admins: Set<User>,

    @Column(name = "street", nullable = false)
    val street: String,

    @Column(name = "postal_code", nullable = false)
    val postalCode: String,

    @Column(name = "city", nullable = false)
    val city: String,

    @Column(name = "latitude")
    val latitude: Double?,

    @Column(name = "longitude")
    val longitude: Double?,
)

enum class OrganizationStatus {
    ENABLED,
    DISABLED;
}

@JvmInline
value class OrganizationId(
    val value: UUID,
)
