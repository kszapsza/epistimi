package pl.edu.wat.wcy.epistimi.grade.domain.port

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters

interface ClassificationGradeRepository {
    fun findAllWithFiltering(gradeFilters: GradeFilters): List<ClassificationGrade>
    fun save(classificationGrade: ClassificationGrade): ClassificationGrade
}
