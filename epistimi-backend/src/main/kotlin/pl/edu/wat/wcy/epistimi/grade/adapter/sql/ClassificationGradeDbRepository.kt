package pl.edu.wat.wcy.epistimi.grade.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.grade.adapter.sql.jpa.ClassificationGradeJpaRepository
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.port.ClassificationGradeRepository

@Repository
class ClassificationGradeDbRepository(
    private val classificationGradeJpaRepository: ClassificationGradeJpaRepository,
) : ClassificationGradeRepository {
    override fun save(classificationGrade: ClassificationGrade): ClassificationGrade {
        return classificationGradeJpaRepository.save(classificationGrade)
    }
}
