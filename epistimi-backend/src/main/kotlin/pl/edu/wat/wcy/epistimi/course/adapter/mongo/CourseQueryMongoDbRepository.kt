package pl.edu.wat.wcy.epistimi.course.adapter.mongo

import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

interface CourseQueryMongoDbRepository {
    fun findAllWithFiltering(
        organizationId: OrganizationId?,
        classTeacherId: TeacherId?,
    ): List<CourseMongoDbDocument>
}
