package pl.edu.wat.wcy.epistimi.noticeboard.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.user.domain.User
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "noticeboard_posts")
class NoticeboardPost(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: NoticeboardPostId? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", updatable = false)
    val organization: Organization,

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", updatable = false)
    val author: User,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val createdAt: LocalDateTime?,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val updatedAt: LocalDateTime?,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    val content: String,
)

@JvmInline
value class NoticeboardPostId(
    val value: UUID,
)
