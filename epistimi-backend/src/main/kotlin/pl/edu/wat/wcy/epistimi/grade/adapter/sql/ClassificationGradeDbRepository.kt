package pl.edu.wat.wcy.epistimi.grade.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.grade.adapter.sql.jpa.ClassificationGradeJpaRepository
import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGrade
import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.port.ClassificationGradeRepository
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import java.util.UUID
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
class ClassificationGradeDbRepository(
    private val classificationGradeJpaRepository: ClassificationGradeJpaRepository,
    private val entityManager: EntityManager,
) : ClassificationGradeRepository {
    override fun findAllWithFiltering(gradeFilters: GradeFilters): List<ClassificationGrade> {
        return createFindAllWithFilteringQuery(gradeFilters).resultList
    }

    private fun createFindAllWithFilteringQuery(gradeFilters: GradeFilters): TypedQuery<ClassificationGrade> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(ClassificationGrade::class.java)
        val gradesRoot = criteriaQuery.from(ClassificationGrade::class.java)

        return entityManager.createQuery(
            criteriaQuery
                .select(gradesRoot)
                .where(*buildPredicates(gradesRoot, gradeFilters))
        )
    }

    private fun buildPredicates(
        gradesRoot: Root<ClassificationGrade>,
        gradeFilters: GradeFilters
    ): Array<Predicate> {
        val (subjectIds, studentIds) = gradeFilters
        return mutableListOf<Predicate>()
            .also { if (!subjectIds.isNullOrEmpty()) it += buildSubjectIdPredicate(gradesRoot, subjectIds) }
            .also { if (!studentIds.isNullOrEmpty()) it += buildStudentIdsPredicate(gradesRoot, studentIds) }
            .toTypedArray()
    }

    private fun buildSubjectIdPredicate(
        gradesRoot: Root<ClassificationGrade>,
        subjectIds: List<SubjectId>,
    ): Predicate {
        val subjectJoin = gradesRoot.join<Grade, Subject>("subject", JoinType.INNER)
        return subjectJoin.get<UUID>("id").`in`(subjectIds.map(SubjectId::value))
    }

    private fun buildStudentIdsPredicate(
        gradesRoot: Root<ClassificationGrade>,
        studentIds: List<StudentId>,
    ): Predicate {
        val studentJoin = gradesRoot.join<Grade, Student>("student", JoinType.INNER)
        return studentJoin.get<UUID>("id").`in`(studentIds.map(StudentId::value))
    }

    override fun save(classificationGrade: ClassificationGrade): ClassificationGrade {
        return classificationGradeJpaRepository.save(classificationGrade)
    }
}
