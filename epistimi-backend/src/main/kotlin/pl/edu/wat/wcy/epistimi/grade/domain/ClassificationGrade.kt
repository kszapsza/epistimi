package pl.edu.wat.wcy.epistimi.grade.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
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
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "classification_grades",
    uniqueConstraints = [UniqueConstraint(columnNames = ["student_id", "subject_id", "period", "is_proposal"])]
)
class ClassificationGrade(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: ClassificationGradeId? = null,

    @ManyToOne
    val subject: Subject,

    @ManyToOne
    val student: Student,

    @ManyToOne
    val issuedBy: Teacher,

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

    @Column(name = "period", nullable = false)
    @Enumerated(EnumType.STRING)
    val period: ClassificationGradePeriod,

    @Column(name = "is_proposal", nullable = false)
    val isProposal: Boolean,
)

@JvmInline
value class ClassificationGradeId(
    val value: UUID,
)

enum class ClassificationGradePeriod {
    FIRST_SEMESTER,
    SECOND_SEMESTER,
    SCHOOL_YEAR;

    companion object {
        fun fromSemesterNumber(semester: Int): ClassificationGradePeriod {
            return when (semester) {
                1 -> FIRST_SEMESTER
                2 -> SECOND_SEMESTER
                else -> SCHOOL_YEAR
            }
        }
    }
}
