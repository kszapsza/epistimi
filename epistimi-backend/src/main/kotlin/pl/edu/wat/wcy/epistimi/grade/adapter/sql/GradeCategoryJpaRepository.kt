package pl.edu.wat.wcy.epistimi.grade.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import java.util.UUID

interface GradeCategoryJpaRepository : JpaRepository<GradeCategory, UUID>
