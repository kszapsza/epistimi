package pl.edu.wat.wcy.epistimi.student.adapter.sql

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.parent.adapter.sql.ParentJpaEntity
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserJpaEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "students")
class StudentJpaEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID? = null,

    @OneToOne
    val user: UserJpaEntity,

    @ManyToMany
    val parents: List<ParentJpaEntity>,
)
