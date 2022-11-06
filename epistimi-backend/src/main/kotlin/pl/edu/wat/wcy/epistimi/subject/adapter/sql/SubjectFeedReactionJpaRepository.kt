package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.subject.SubjectFeedReaction
import pl.edu.wat.wcy.epistimi.subject.SubjectPostReactionId

interface SubjectFeedReactionJpaRepository : JpaRepository<SubjectFeedReaction, SubjectPostReactionId>
