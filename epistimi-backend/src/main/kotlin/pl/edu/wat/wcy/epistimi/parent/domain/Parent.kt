package pl.edu.wat.wcy.epistimi.parent.domain

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "parents")
class Parent(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: ParentId? = null,

    @OneToOne
    val user: User,
)

@JvmInline
value class ParentId(
    val value: UUID,
)
