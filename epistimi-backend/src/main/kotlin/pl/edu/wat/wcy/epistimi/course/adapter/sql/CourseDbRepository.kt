package pl.edu.wat.wcy.epistimi.course.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseNotFoundException
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationJpaEntity
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.adapter.sql.TeacherJpaEntity
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
        return DbHandlers.handleDbGet(mapper = CourseDbBiMapper) {
            courseJpaRepository.findById(courseId.value)
                .orElseThrow { CourseNotFoundException(courseId) }
        }
    }

    override fun findAllWithFiltering(
        organizationId: OrganizationId,
        classTeacherId: TeacherId?,
    ): List<Course> {
        return DbHandlers.handleDbMultiGet(mapper = CourseDbBiMapper) {
            createTypedQuery(organizationId, classTeacherId).resultList
        }
    }

    private fun createTypedQuery(
        organizationId: OrganizationId?,
        classTeacherId: TeacherId?,
    ): TypedQuery<CourseJpaEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(CourseJpaEntity::class.java)
        val coursesRoot = criteriaQuery.from(CourseJpaEntity::class.java)

        val predicates = mutableListOf<Predicate>()
            .also { if (organizationId != null) it += buildOrganizationPredicate(criteriaBuilder, coursesRoot, organizationId) }
            .also { if (classTeacherId != null) it += buildClassTeacherPredicate(criteriaBuilder, coursesRoot, classTeacherId) }
            .toTypedArray()

        return entityManager.createQuery(
            criteriaQuery
                .select(coursesRoot)
                .where(*predicates)
        )
    }

    private fun buildOrganizationPredicate(
        criteriaBuilder: CriteriaBuilder,
        coursesRoot: Root<CourseJpaEntity>,
        organizationId: OrganizationId,
    ): Predicate {
        val organizationJoin = coursesRoot.join<CourseJpaEntity, OrganizationJpaEntity>("organization", JoinType.INNER)
        return criteriaBuilder.equal(organizationJoin.get<UUID>("id"), organizationId.value)
    }

    private fun buildClassTeacherPredicate(
        criteriaBuilder: CriteriaBuilder,
        coursesRoot: Root<CourseJpaEntity>,
        classTeacherId: TeacherId,
    ): Predicate {
        val classTeacherJoin = coursesRoot.join<OrganizationJpaEntity, TeacherJpaEntity>("classTeacher", JoinType.INNER)
        return criteriaBuilder.equal(classTeacherJoin.get<UUID>("id"), classTeacherId.value)
    }

    override fun save(course: Course): Course {
        return DbHandlers.handleDbInsert(
            domainObject = course,
            mapper = CourseDbBiMapper,
            dbCall = courseJpaRepository::save,
        )
    }
}
