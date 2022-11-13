package pl.edu.wat.wcy.epistimi.course.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.course.domain.Course
import java.util.UUID

interface CourseJpaRepository : JpaRepository<Course, UUID>
