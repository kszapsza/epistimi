package pl.edu.wat.wcy.epistimi.grade.domain.port

import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId

interface GradeCategoryRepository {
    fun findById(gradeCategoryId: GradeCategoryId): GradeCategory
    fun save(gradeCategory: GradeCategory): GradeCategory
}
