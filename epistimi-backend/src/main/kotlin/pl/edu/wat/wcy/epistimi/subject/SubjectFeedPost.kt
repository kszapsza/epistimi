package pl.edu.wat.wcy.epistimi.subject

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.user.User
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "subject_feed_posts")
class SubjectFeedPost(
    @Id
    val id: SubjectFeedEntityId? = null,

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    val entity: SubjectFeedEntity,

    @OneToMany(mappedBy = "post")
    val comments: Set<SubjectFeedComment>,

    @ManyToOne
    val subject: Subject,

    @ManyToOne
    val author: User,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val createdAt: LocalDateTime?,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val updatedAt: LocalDateTime?,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    val content: String,
)
