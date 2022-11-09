package pl.edu.wat.wcy.epistimi.grade.port

import pl.edu.wat.wcy.epistimi.grade.Grade
import pl.edu.wat.wcy.epistimi.grade.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.GradeId

interface GradeRepository {
    fun findById(gradeId: GradeId): Grade
    fun findAllWithFiltering(gradeFilters: GradeFilters): List<Grade>
}
