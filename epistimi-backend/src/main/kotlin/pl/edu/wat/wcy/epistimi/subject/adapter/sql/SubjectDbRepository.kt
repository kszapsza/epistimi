package pl.edu.wat.wcy.epistimi.subject.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.adapter.sql.ParentJpaRepository
import pl.edu.wat.wcy.epistimi.student.adapter.sql.StudentJpaRepository
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectNotFoundException
import pl.edu.wat.wcy.epistimi.subject.domain.port.SubjectRepository
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import java.util.UUID
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
class SubjectDbRepository(
    private val subjectJpaRepository: SubjectJpaRepository,
    private val studentJpaRepository: StudentJpaRepository,
    private val parentJpaRepository: ParentJpaRepository,
    private val entityManager: EntityManager,
) : SubjectRepository {

    override fun findById(subjectId: SubjectId): Subject {
        return subjectJpaRepository.findById(subjectId.value)
            .orElseThrow { SubjectNotFoundException(subjectId) }
    }

    override fun findAllByOrganizationId(organizationId: OrganizationId): List<Subject> {
        return createFindAllByOrganizationIdQuery(organizationId).resultList
    }

    private fun createFindAllByOrganizationIdQuery(organizationId: OrganizationId): TypedQuery<Subject> {
        val criteriaBuilder: CriteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery: CriteriaQuery<Subject> = criteriaBuilder.createQuery(Subject::class.java)
        val subjectsRoot: Root<Subject> = criteriaQuery.from(Subject::class.java)

        return entityManager.createQuery(
            criteriaQuery
                .select(subjectsRoot)
                .where(buildOrganizationIdPredicate(criteriaBuilder, subjectsRoot, organizationId))
        )
    }

    private fun buildOrganizationIdPredicate(
        criteriaBuilder: CriteriaBuilder,
        subjectsRoot: Root<Subject>,
        organizationId: OrganizationId,
    ): Predicate {
        val courseJoin = subjectsRoot.join<Subject, Course>("course", JoinType.INNER)
        val organizationJoin = courseJoin.join<Course, Organization>("organization", JoinType.INNER)
        return criteriaBuilder.equal(organizationJoin.get<UUID>("id"), organizationId.value)
    }

    override fun findAllByTeacherUserId(teacherUserId: UserId): List<Subject> {
        return createFindAllByTeacherUserIdQuery(teacherUserId).resultList
    }

    private fun createFindAllByTeacherUserIdQuery(teacherUserId: UserId): TypedQuery<Subject> {
        val criteriaBuilder: CriteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery: CriteriaQuery<Subject> = criteriaBuilder.createQuery(Subject::class.java)
        val subjectsRoot: Root<Subject> = criteriaQuery.from(Subject::class.java)

        return entityManager.createQuery(
            criteriaQuery
                .select(subjectsRoot)
                .where(buildTeacherUserIdPredicate(criteriaBuilder, subjectsRoot, teacherUserId))
        )
    }

    private fun buildTeacherUserIdPredicate(
        criteriaBuilder: CriteriaBuilder,
        subjectsRoot: Root<Subject>,
        teacherUserId: UserId,
    ): Predicate {
        val teacherJoin = subjectsRoot.join<Subject, Teacher>("teacher", JoinType.INNER)
        val teacherUserJoin = teacherJoin.join<Teacher, User>("user", JoinType.INNER)
        return criteriaBuilder.equal(teacherUserJoin.get<UUID>("id"), teacherUserId.value)
    }

    override fun findAllByStudentUserId(studentUserId: UserId): List<Subject> {
        return studentJpaRepository.findByUserId(studentUserId.value)
            ?.course?.subjects?.toList()
            ?: emptyList()
    }

    override fun findAllByParentUserId(parentUserId: UserId): List<Subject> {
        return parentJpaRepository.findByUserId(parentUserId.value)
            ?.students?.map { it.course }?.flatMap { it.subjects }
            ?: emptyList()
    }

    override fun save(subject: Subject): Subject {
        return subjectJpaRepository.save(subject)
    }
}
