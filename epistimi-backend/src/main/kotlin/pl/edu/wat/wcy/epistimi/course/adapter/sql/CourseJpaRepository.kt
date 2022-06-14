package pl.edu.wat.wcy.epistimi.course.adapter.sql;

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CourseJpaRepository : JpaRepository<CourseJpaEntity, UUID>