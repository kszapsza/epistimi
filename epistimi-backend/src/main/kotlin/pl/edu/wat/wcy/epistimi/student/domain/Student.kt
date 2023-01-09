package pl.edu.wat.wcy.epistimi.student.domain

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
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

    @ManyToOne
    @JoinColumn(name = "course_id")
    val course: Course,

    @OneToOne
    val user: User,

    @ManyToMany
    @JoinTable(
        name = "students_parents",
        joinColumns = [ JoinColumn(name = "student_id") ],
        inverseJoinColumns = [ JoinColumn(name = "parent_id") ],
    )
    val parents: List<Parent>,
)

@JvmInline
value class StudentId(
    val value: UUID,
)
