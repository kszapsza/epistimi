package pl.edu.wat.wcy.epistimi.course.adapter.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseNotFoundException
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers

@Repository
class CourseDbRepository(
    private val courseMongoDbRepository: CourseMongoDbRepository,
    private val organizationRepository: OrganizationRepository,
) : CourseRepository {

    override fun findById(courseId: CourseId): Course {
        return DbHandlers.handleDbGet(mapper = CourseDbBiMapper) {
            courseMongoDbRepository.findById(courseId.value)
                .orElseThrow { CourseNotFoundException(courseId) }
        }
    }

    override fun findAll(organizationId: OrganizationId): List<Course> {
        return DbHandlers.handleDbMultiGet(mapper = CourseDbBiMapper) {
            if (!organizationRepository.exists(organizationId)) {
                throw OrganizationNotFoundException(organizationId)
            }
            courseMongoDbRepository.findAllByOrganizationId(organizationId.value)
        }
    }

    override fun save(course: Course): Course {
        return DbHandlers.handleDbInsert(
            domainObject = course,
            mapper = CourseDbBiMapper,
            dbCall = courseMongoDbRepository::save,
        )
    }
}
