package pl.edu.wat.wcy.epistimi.grade.adapter.sql

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.wat.wcy.epistimi.grade.Grade
import pl.edu.wat.wcy.epistimi.grade.GradeId

interface GradeJpaRepository : JpaRepository<Grade, GradeId>
