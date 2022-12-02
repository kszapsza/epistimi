package pl.edu.wat.wcy.epistimi.grade.domain.port

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade

interface ClassificationGradeRepository {
    fun save(classificationGrade: ClassificationGrade): ClassificationGrade
}
