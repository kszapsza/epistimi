package pl.edu.wat.wcy.epistimi.teacher.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class TeacherDbRepository(
    private val teacherMongoDbRepository: TeacherMongoDbRepository,
) : TeacherRepository {

    override fun findById(id: TeacherId): Teacher {
        return teacherMongoDbRepository.findById(id.value)
            .orElseThrow { TeacherNotFoundException(id) }
            .toDomain()
    }

    private fun TeacherMongoDbDocument.toDomain() = Teacher(
        id = TeacherId(id!!),
        userId = UserId(userId),
        organizationId = OrganizationId(organizationId),
        academicTitle = academicTitle,
    )

    override fun findByUserId(id: UserId): Teacher {
        return teacherMongoDbRepository.findFirstByUserId(id.value)
            ?.toDomain()
            ?: throw TeacherNotFoundException()
    }

    override fun findAll(organizationId: OrganizationId): List<Teacher> {
        return teacherMongoDbRepository.findAllByOrganizationId(organizationId.value)
            .map { it.toDomain() }
    }

    override fun save(teacher: Teacher): Teacher {
        return teacherMongoDbRepository.save(
            TeacherMongoDbDocument(
                id = null,
                userId = teacher.userId.value,
                organizationId = teacher.organizationId.value,
                academicTitle = teacher.academicTitle,
            )
        ).toDomain()
    }
}
