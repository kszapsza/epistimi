package pl.edu.wat.wcy.epistimi.student.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.student.Student
import java.util.UUID

interface StudentJpaRepository : JpaRepository<Student, UUID> {
    fun findFirstByUserId(userId: UUID): Student?
}
