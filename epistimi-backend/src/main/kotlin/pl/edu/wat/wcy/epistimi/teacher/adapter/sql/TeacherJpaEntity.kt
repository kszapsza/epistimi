package pl.edu.wat.wcy.epistimi.teacher.adapter.sql

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationJpaEntity
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserJpaEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "teachers")
class TeacherJpaEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID? = null,

    @OneToOne
    val user: UserJpaEntity,

    @ManyToOne
    val organization: OrganizationJpaEntity,

    @Column
    val academicTitle: String?,
)