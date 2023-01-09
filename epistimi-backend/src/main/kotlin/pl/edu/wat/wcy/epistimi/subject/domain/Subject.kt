package pl.edu.wat.wcy.epistimi.subject.domain

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "subjects")
class Subject(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: SubjectId? = null,

    @ManyToOne
    val course: Course,

    @ManyToOne
    val teacher: Teacher,

    @Column(name = "name", nullable = false)
    val name: String,
)

@JvmInline
value class SubjectId(
    val value: UUID,
)
