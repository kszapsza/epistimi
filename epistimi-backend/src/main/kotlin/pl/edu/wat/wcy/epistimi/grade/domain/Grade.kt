package pl.edu.wat.wcy.epistimi.grade.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
@Table(name = "grades")
class Grade(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: GradeId? = null,

    @ManyToOne
    val subject: Subject,

    @ManyToOne
    val student: Student,

    @ManyToOne
    val issuedBy: Teacher,

    @ManyToOne
    val category: GradeCategory,

    @Column(name = "semester")
    @Min(1)
    @Max(2)
    val semester: Int,

    @CreationTimestamp
    @Column(name = "issued_at", nullable = false, insertable = true, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val issuedAt: LocalDateTime?,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, insertable = false, updatable = true, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val updatedAt: LocalDateTime?,

    @Column(name = "value", nullable = false)
    @Enumerated(EnumType.STRING)
    val value: GradeValue,

    @Column(name = "weight", nullable = false)
    val weight: Int,

    @Column(name = "count_towards_average", nullable = false)
    val countTowardsAverage: Boolean,

    @Column(name = "comment", columnDefinition = "TEXT")
    val comment: String?,
) {
    infix fun isIssuedBy(user: User): Boolean = this.issuedBy.user.id == user.id
    infix fun isIssuedFor(user: User): Boolean = this.student.user.id == user.id
}

@JvmInline
value class GradeId(
    val value: UUID,
)
