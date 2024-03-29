package pl.edu.wat.wcy.epistimi.student.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.student.domain.Student
import java.util.UUID

interface StudentJpaRepository : JpaRepository<Student, UUID> {
    fun findByUserId(userId: UUID): Student?
}
