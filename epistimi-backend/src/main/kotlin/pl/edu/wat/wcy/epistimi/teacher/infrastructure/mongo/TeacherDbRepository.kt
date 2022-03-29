package pl.edu.wat.wcy.epistimi.teacher.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Repository
class TeacherDbRepository(
    private val teacherMongoDbRepository: TeacherMongoDbRepository,
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository,
) : TeacherRepository {

    override fun findById(id: TeacherId): Teacher {
        return teacherMongoDbRepository.findById(id.value)
            .orElseThrow { TeacherNotFoundException(id) }
            .toDomain()
    }

    private fun TeacherMongoDbDocument.toDomain() = Teacher(
        id = TeacherId(id!!),
        user = userRepository.findById(UserId(userId)),
        organization = organizationRepository.findById(OrganizationId(organizationId)),
        academicTitle = academicTitle,
    )
}
