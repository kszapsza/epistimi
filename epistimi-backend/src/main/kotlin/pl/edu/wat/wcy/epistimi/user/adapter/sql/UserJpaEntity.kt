package pl.edu.wat.wcy.epistimi.user.adapter.sql

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "epistimi_users")
class UserJpaEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID? = null,

    @Column val firstName: String,
    @Column val lastName: String,
    @Column val role: String,

    @Column(unique = true)
    val username: String,

    @Column val passwordHash: String,
    @Column val pesel: String?,
    @Column val sex: String?,
    @Column val email: String?,
    @Column val phoneNumber: String?,

    @Column val street: String?,
    @Column val postalCode: String?,
    @Column val city: String?,
)
