package pl.edu.wat.wcy.epistimi.teacher

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.user.User
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "teachers")
class Teacher(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: TeacherId? = null,

    @OneToOne
    val user: User,

    @Column
    val academicTitle: String?,
)

@JvmInline
value class TeacherId(
    val value: UUID,
)
