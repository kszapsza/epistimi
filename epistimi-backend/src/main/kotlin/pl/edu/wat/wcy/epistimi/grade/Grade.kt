package pl.edu.wat.wcy.epistimi.grade

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.subject.Subject
import pl.edu.wat.wcy.epistimi.teacher.Teacher
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

@Entity
@Table(name = "grades")
data class Grade(
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

    @CreationTimestamp
    @Column(name = "issued_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val issuedAt: LocalDateTime?,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
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
)

@JvmInline
value class GradeId(
    val value: UUID,
)

enum class GradeValue(
    val displayName: String,
    val fullName: String,
    val numericValue: Int?,
) {
    UNSATISFACTORY("1", "niedostateczny", 1),
    ACCEPTABLE_MINUS("2-", "dopuszczający minus", 2),
    ACCEPTABLE("2", "dopuszczający", 2),
    ACCEPTABLE_PLUS("2+", "dopuszczający plus", 2),
    SATISFACTORY_MINUS("3-", "dostateczny minus", 3),
    SATISFACTORY("3", "dostateczny", 3),
    SATISFACTORY_PLUS("3+", "dostateczny plus", 3),
    GOOD_MINUS("4-", "dobry minus", 4),
    GOOD("4", "dobry", 4),
    GOOD_PLUS("4+", "dobry plus", 4),
    VERY_GOOD_MINUS("5-", "bardzo dobry minus", 5),
    VERY_GOOD("5", "bardzo dobry", 5),
    VERY_GOOD_PLUS("5+", "bardzo dobry plus", 5),
    EXCELLENT("6", "celujący", 6),
    NO_ASSIGNMENT("bz", "brak zadania", null),
    UNPREPARED("np", "nieprzygotowany", null),
    UNCLASSIFIED("nk", "nieklasyfikowany", null),
    ATTENDED("uc", "uczęszczał", null),
    DID_NOT_ATTEND("nu", "nie uczęszczał", null),
    PASSED("zl", "zaliczył", null),
    FAILED("nz", "nie zaliczył", null),
    ABSENT("nb", "nieobecny", null),
    EXEMPT("zw", "zwolniony", null);
}
