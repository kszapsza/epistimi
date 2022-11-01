package pl.edu.wat.wcy.epistimi.student

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.user.User
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
class Student(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: StudentId? = null,

    @OneToOne
    val user: User,

    @ManyToMany
    val parents: List<Parent>,
)

@JvmInline
value class StudentId(
    val value: UUID,
)
