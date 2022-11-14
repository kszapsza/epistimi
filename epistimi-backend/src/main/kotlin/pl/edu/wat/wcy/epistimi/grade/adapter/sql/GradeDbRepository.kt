package pl.edu.wat.wcy.epistimi.grade.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
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
class GradeDbRepository(
    private val gradeJpaRepository: GradeJpaRepository,
    private val entityManager: EntityManager,
) : GradeRepository {

    override fun findById(gradeId: GradeId): Grade {
        return gradeJpaRepository.findById(gradeId.value)
            .orElseThrow { GradeNotFoundException(gradeId) }
    }

    override fun findAllWithFiltering(gradeFilters: GradeFilters): List<Grade> {
        return createFindAllWithFilteringQuery(gradeFilters).resultList
    }

    private fun createFindAllWithFilteringQuery(gradeFilters: GradeFilters): TypedQuery<Grade> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Grade::class.java)
        val gradesRoot = criteriaQuery.from(Grade::class.java)

        return entityManager.createQuery(
            criteriaQuery
                .select(gradesRoot)
                .where(*buildPredicates(gradesRoot, gradeFilters))
        )
    }

    private fun buildPredicates(
        gradesRoot: Root<Grade>,
        gradeFilters: GradeFilters
    ): Array<Predicate> {
        val (subjectIds, studentIds) = gradeFilters
        return mutableListOf<Predicate>()
            .also { if (!subjectIds.isNullOrEmpty()) it += buildSubjectIdPredicate(gradesRoot, subjectIds) }
            .also { if (!studentIds.isNullOrEmpty()) it += buildStudentIdsPredicate(gradesRoot, studentIds) }
            .toTypedArray()
    }

    private fun buildSubjectIdPredicate(
        gradesRoot: Root<Grade>,
        subjectIds: List<SubjectId>,
    ): Predicate {
        val subjectJoin = gradesRoot.join<Grade, Subject>("subject", JoinType.INNER)
        return subjectJoin.get<UUID>("id").`in`(subjectIds.map(SubjectId::value))
    }

    private fun buildStudentIdsPredicate(
        gradesRoot: Root<Grade>,
        studentIds: List<StudentId>,
    ): Predicate {
        val studentJoin = gradesRoot.join<Grade, Student>("student", JoinType.INNER)
        return studentJoin.get<UUID>("id").`in`(studentIds.map(StudentId::value))
    }

    override fun save(grade: Grade): Grade {
        return gradeJpaRepository.save(grade)
    }
}
