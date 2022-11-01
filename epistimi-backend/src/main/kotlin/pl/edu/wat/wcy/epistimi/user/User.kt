package pl.edu.wat.wcy.epistimi.user

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.common.api.toUuid
import pl.edu.wat.wcy.epistimi.organization.Organization
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "epistimi_users")
class User(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: UserId? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    val organization: Organization,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "role", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole,

    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "pesel", length = 11)
    val pesel: String?,

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    val sex: UserSex?,

    @Column(name = "email")
    val email: String?,

    @Column(name = "phone_number")
    val phoneNumber: String?,

    @Column(name = "street")
    val street: String?,

    @Column(name = "postal_code", length = 6)
    val postalCode: String?,

    @Column(name = "city")
    val city: String?,
)


@JvmInline
value class UserId(
    val value: UUID,
) {
    constructor(value: String) : this(value = value.toUuid())
}

enum class UserRole {
    EPISTIMI_ADMIN,
    ORGANIZATION_ADMIN,
    TEACHER,
    STUDENT,
    PARENT;
}

enum class UserSex {
    MALE,
    FEMALE,
    OTHER;
}
