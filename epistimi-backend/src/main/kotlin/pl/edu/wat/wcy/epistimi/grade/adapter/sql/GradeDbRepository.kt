package pl.edu.wat.wcy.epistimi.grade.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.grade.Grade
import pl.edu.wat.wcy.epistimi.grade.GradeFilters
import pl.edu.wat.wcy.epistimi.grade.GradeId
import pl.edu.wat.wcy.epistimi.grade.GradeNotFoundException
import pl.edu.wat.wcy.epistimi.grade.port.GradeRepository
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.subject.Subject
import pl.edu.wat.wcy.epistimi.subject.SubjectId
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
        return gradeJpaRepository.findById(gradeId)
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
                .where(*createPredicates(gradesRoot, gradeFilters))
        )
    }

    private fun createPredicates(
        gradesRoot: Root<Grade>,
        gradeFilters: GradeFilters
    ): Array<Predicate> {
        val (subjectId, studentIds) = gradeFilters
        return mutableListOf<Predicate>()
            .also { it += buildSubjectIdPredicate(gradesRoot, subjectId) }
            .also { if (studentIds != null) it += buildStudentIdsPredicate(gradesRoot, studentIds) }
            .toTypedArray()
    }

    private fun buildSubjectIdPredicate(
        gradesRoot: Root<Grade>,
        subjectId: SubjectId,
    ): Predicate {
        val subjectJoin = gradesRoot.join<Grade, Subject>("subject", JoinType.INNER)
        return subjectJoin.get<UUID>("id").`in`(subjectId.value)
    }

    private fun buildStudentIdsPredicate(
        gradesRoot: Root<Grade>,
        studentIds: List<StudentId>,
    ): Predicate {
        val studentJoin = gradesRoot.join<Grade, Student>("student", JoinType.INNER)
        return studentJoin.get<UUID>("id").`in`(studentIds.map(StudentId::value))
    }
}
