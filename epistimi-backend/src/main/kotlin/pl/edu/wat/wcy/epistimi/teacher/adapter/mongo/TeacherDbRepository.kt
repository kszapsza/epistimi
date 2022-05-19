package pl.edu.wat.wcy.epistimi.teacher.adapter.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class TeacherDbRepository(
    private val teacherMongoDbRepository: TeacherMongoDbRepository,
) : TeacherRepository {

    override fun findById(id: TeacherId): Teacher {
        return DbHandlers.handleDbGet(mapper = TeacherDbBiMapper) {
            teacherMongoDbRepository.findById(id.value)
                .orElseThrow { TeacherNotFoundException(id) }
        }
    }

    override fun findByUserId(id: UserId): Teacher {
        return DbHandlers.handleDbGet(mapper = TeacherDbBiMapper) {
            teacherMongoDbRepository.findFirstByUserId(id.value)
                ?: throw TeacherNotFoundException()
        }
    }

    override fun findAll(organizationId: OrganizationId): List<Teacher> {
        return DbHandlers.handleDbMultiGet(mapper = TeacherDbBiMapper) {
            teacherMongoDbRepository.findAllByOrganizationId(organizationId.value)
        }
    }

    override fun save(teacher: Teacher): Teacher {
        return DbHandlers.handleDbInsert(
            domainObject = teacher,
            mapper = TeacherDbBiMapper,
            dbCall = teacherMongoDbRepository::save,
        )
    }
}
