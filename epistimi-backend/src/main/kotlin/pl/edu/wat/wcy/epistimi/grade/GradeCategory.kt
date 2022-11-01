package pl.edu.wat.wcy.epistimi.grade

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.subject.Subject
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "grade_categories")
data class GradeCategory(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: GradeCategoryId,

    @ManyToOne
    val subject: Subject,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "default_weight", nullable = false)
    val defaultWeight: Int,

    @Column(name = "color")
    val color: String? = null,
)

@JvmInline
value class GradeCategoryId(
    val value: UUID,
)
