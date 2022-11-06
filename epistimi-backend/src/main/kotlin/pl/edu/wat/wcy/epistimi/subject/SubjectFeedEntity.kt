package pl.edu.wat.wcy.epistimi.subject

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "subject_feed_entities")
class SubjectFeedEntity private constructor(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: SubjectFeedEntityId,

    @OneToMany(mappedBy = "entity")
    val reactions: Set<SubjectFeedReaction>,
)

@JvmInline
value class SubjectFeedEntityId(
    val value: UUID,
)
