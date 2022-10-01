package pl.edu.wat.wcy.epistimi.noticeboard.adapter.sql

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationJpaEntity
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserJpaEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "noticeboard_posts")
class NoticeboardPostJpaEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID? = null,

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    val organization: OrganizationJpaEntity,

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    val author: UserJpaEntity,

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val createdAt: LocalDateTime?,

    @UpdateTimestamp
    @Column(nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter::class)
    val updatedAt: LocalDateTime?,

    @Column
    val title: String,

    @Column(columnDefinition = "TEXT")
    val content: String,
)