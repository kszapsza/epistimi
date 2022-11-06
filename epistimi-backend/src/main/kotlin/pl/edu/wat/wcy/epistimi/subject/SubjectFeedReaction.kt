package pl.edu.wat.wcy.epistimi.subject

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.user.User
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
@Table(name = "subject_feed_reactions")
class SubjectFeedReaction(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: SubjectPostReactionId? = null,

    @ManyToOne
    val entity: SubjectFeedEntity,

    @ManyToOne
    val author: User,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: SubjectPostReactionType,

    @CreationTimestamp
    @Column(name = "reacted_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val reactedAt: LocalDateTime? = null,
)

@JvmInline
value class SubjectPostReactionId(
    val value: UUID,
)

enum class SubjectPostReactionType {
    LIKE,
    LOVE,
    WOW,
    HAHA,
    SORRY,
    ANGRY;
}

