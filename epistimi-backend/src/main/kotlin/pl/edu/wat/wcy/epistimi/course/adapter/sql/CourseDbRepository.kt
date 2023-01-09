package pl.edu.wat.wcy.epistimi.course.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.course.domain.CourseNotFoundException
import pl.edu.wat.wcy.epistimi.course.domain.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import java.util.UUID
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
class CourseDbRepository(
    private val courseJpaRepository: CourseJpaRepository,
    private val entityManager: EntityManager,
) : CourseRepository {

    override fun findById(courseId: CourseId): Course {
        return courseJpaRepository.findById(courseId.value)
            .orElseThrow { CourseNotFoundException(courseId) }
    }

    override fun findAllWithFiltering(
        organizationId: OrganizationId,
        classTeacherId: TeacherId?,
    ): List<Course> {
        return createFindAllWithFilteringQuery(organizationId, classTeacherId).resultList
    }

    private fun createFindAllWithFilteringQuery(
        organizationId: OrganizationId?,
        classTeacherId: TeacherId?,
    ): TypedQuery<Course> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Course::class.java)
        val coursesRoot = criteriaQuery.from(Course::class.java)

        val predicates = mutableListOf<Predicate>()
            .also { if (organizationId != null) it += buildOrganizationIdPredicate(criteriaBuilder, coursesRoot, organizationId) }
            .also { if (classTeacherId != null) it += buildClassTeacherIdPredicate(criteriaBuilder, coursesRoot, classTeacherId) }
            .toTypedArray()

        return entityManager.createQuery(
            criteriaQuery
                .select(coursesRoot)
                .where(*predicates)
        )
    }

    private fun buildOrganizationIdPredicate(
        criteriaBuilder: CriteriaBuilder,
        coursesRoot: Root<Course>,
        organizationId: OrganizationId,
    ): Predicate {
        val organizationJoin = coursesRoot.join<Course, Organization>("organization", JoinType.INNER)
        return criteriaBuilder.equal(organizationJoin.get<UUID>("id"), organizationId.value)
    }

    private fun buildClassTeacherIdPredicate(
        criteriaBuilder: CriteriaBuilder,
        coursesRoot: Root<Course>,
        classTeacherId: TeacherId,
    ): Predicate {
        val classTeacherJoin = coursesRoot.join<Course, Teacher>("classTeacher", JoinType.INNER)
        return criteriaBuilder.equal(classTeacherJoin.get<UUID>("id"), classTeacherId.value)
    }

    override fun save(course: Course): Course {
        return courseJpaRepository.save(course)
    }
}
