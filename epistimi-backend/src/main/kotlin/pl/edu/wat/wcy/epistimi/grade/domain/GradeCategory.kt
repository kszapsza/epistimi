package pl.edu.wat.wcy.epistimi.grade.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
@Table(name = "grade_categories")
class GradeCategory(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: GradeCategoryId? = null,

    @ManyToOne
    val subject: Subject,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "default_weight", nullable = false)
    @Min(1)
    @Max(10)
    val defaultWeight: Int,

    @Column(name = "color")
    val color: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val createdAt: LocalDateTime?,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, insertable = false, updatable = true, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val updatedAt: LocalDateTime?,
)

@JvmInline
value class GradeCategoryId(
    val value: UUID,
)
