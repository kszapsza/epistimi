package pl.edu.wat.wcy.epistimi.grade.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeCategoryRepository
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId

@Repository
class GradeCategoryDbRepository(
    private val gradeCategoryJpaRepository: GradeCategoryJpaRepository,
) : GradeCategoryRepository {

    override fun findById(gradeCategoryId: GradeCategoryId): GradeCategory {
        return gradeCategoryJpaRepository.findById(gradeCategoryId.value)
            .orElseThrow { GradeCategoryNotFoundException(gradeCategoryId) }
    }

    override fun findAllBySubjectId(subjectId: SubjectId): List<GradeCategory> {
        return gradeCategoryJpaRepository.findAllBySubjectId(subjectId.value)
    }

    override fun save(gradeCategory: GradeCategory): GradeCategory {
        return gradeCategoryJpaRepository.save(gradeCategory)
    }
}
