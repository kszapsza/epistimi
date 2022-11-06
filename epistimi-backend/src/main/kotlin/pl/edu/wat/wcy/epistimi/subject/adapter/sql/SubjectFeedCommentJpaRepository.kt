package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.subject.SubjectFeedComment
import pl.edu.wat.wcy.epistimi.subject.SubjectFeedEntityId

interface SubjectFeedCommentJpaRepository : JpaRepository<SubjectFeedComment, SubjectFeedEntityId>
