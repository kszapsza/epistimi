package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import java.util.UUID

interface SubjectJpaRepository : JpaRepository<Subject, UUID>
