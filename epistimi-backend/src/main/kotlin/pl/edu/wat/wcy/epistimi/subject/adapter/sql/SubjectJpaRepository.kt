package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.subject.Subject
import pl.edu.wat.wcy.epistimi.subject.SubjectId

interface SubjectJpaRepository : JpaRepository<Subject, SubjectId>
