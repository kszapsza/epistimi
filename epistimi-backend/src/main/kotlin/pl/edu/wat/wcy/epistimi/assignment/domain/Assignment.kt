package pl.edu.wat.wcy.epistimi.assignment.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.student.domain.Student
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "assignments")
class Assignment(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: AssignmentId? = null,

    @ManyToOne
    val student: Student,

    @ManyToOne
    val course: Course,

    @CreationTimestamp
    @Column(name = "issued_at", nullable = false, insertable = true, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    val issuedAt: LocalDateTime?,


)

@JvmInline
value class AssignmentId(
    val value: UUID,
)