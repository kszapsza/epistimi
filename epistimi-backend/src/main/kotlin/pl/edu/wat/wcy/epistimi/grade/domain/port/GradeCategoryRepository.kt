package pl.edu.wat.wcy.epistimi.grade.domain.port

import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId

interface GradeCategoryRepository {
    fun findById(gradeCategoryId: GradeCategoryId): GradeCategory
    fun findAllBySubjectId(subjectId: SubjectId): List<GradeCategory>
    fun save(gradeCategory: GradeCategory): GradeCategory
}
