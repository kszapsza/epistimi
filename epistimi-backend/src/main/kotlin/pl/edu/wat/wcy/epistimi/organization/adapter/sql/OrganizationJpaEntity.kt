package pl.edu.wat.wcy.epistimi.organization.adapter.sql

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserJpaEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "organizations")
class OrganizationJpaEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID? = null,

    @Column val name: String,
    @Column val status: String,

    @OneToOne
    @JoinColumn(name = "admin_id", updatable = false, insertable = false)
    val admin: UserJpaEntity,

    @Column val street: String,
    @Column val postalCode: String,
    @Column val city: String,

    @Column val latitude: Double?,
    @Column val longitude: Double?,
)
