package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.subject.SubjectFeedEntityId
import pl.edu.wat.wcy.epistimi.subject.SubjectFeedPost

interface SubjectFeedPostJpaRepository : JpaRepository<SubjectFeedPost, SubjectFeedEntityId>
