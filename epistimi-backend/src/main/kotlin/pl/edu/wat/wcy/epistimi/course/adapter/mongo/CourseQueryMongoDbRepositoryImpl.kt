package pl.edu.wat.wcy.epistimi.course.adapter.mongo

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

@Component
class CourseQueryMongoDbRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
) : CourseQueryMongoDbRepository {

    override fun findAllWithFiltering(
        organizationId: OrganizationId?,
        classTeacherId: TeacherId?,
    ): List<CourseMongoDbDocument> {
        return buildQuery(organizationId, classTeacherId)
            .let { query -> mongoTemplate.find(query, CourseMongoDbDocument::class.java) }
    }

    private fun buildQuery(
        organizationId: OrganizationId?,
        classTeacherId: TeacherId?,
    ): Query {
        return Query().also { query ->
            organizationId?.let { query.addCriteria(Criteria.where("organizationId").`is`(organizationId.value)) }
            classTeacherId?.let { query.addCriteria(Criteria.where("classTeacherId").`is`(classTeacherId.value)) }
        }
    }
}
