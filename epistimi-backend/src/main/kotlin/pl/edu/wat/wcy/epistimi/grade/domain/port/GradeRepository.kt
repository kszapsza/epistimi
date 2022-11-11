package pl.edu.wat.wcy.epistimi.grade.domain.port

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId

interface GradeRepository {
    fun findById(gradeId: GradeId): Grade
    fun findAllWithFiltering(gradeFilters: GradeFilters): List<Grade>
}
