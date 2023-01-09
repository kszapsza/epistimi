package pl.edu.wat.wcy.epistimi.grade.adapter.sql.jpa

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import java.util.UUID

interface GradeJpaRepository : JpaRepository<Grade, UUID>
