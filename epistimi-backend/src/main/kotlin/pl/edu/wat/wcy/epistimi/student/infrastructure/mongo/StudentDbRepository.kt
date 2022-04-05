package pl.edu.wat.wcy.epistimi.student.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.ParentRepository
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.StudentRepository
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Repository
class StudentDbRepository(
    private val studentMongoDbRepository: StudentMongoDbRepository,
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository,
    private val parentRepository: ParentRepository,
) : StudentRepository {

    override fun findByIds(ids: List<StudentId>): List<Student> {
        return studentMongoDbRepository.findAllById(ids.map { it.value })
            .map { it.toDomain() }
            .toList()
    }

    private fun StudentMongoDbDocument.toDomain() = Student(
        id = StudentId(id!!),
        user = userRepository.findById(UserId(userId)),
        organization = organizationRepository.findById(OrganizationId(organizationId)),
        parents = parentRepository.findByIds(parentIds.map { ParentId(it) })
    )

    override fun save(student: Student): Student {
        return student.run {
            studentMongoDbRepository.save(
                StudentMongoDbDocument(
                    id = null,
                    userId = user.id!!.value,
                    organizationId = organization.id!!.value,
                    parentIds = parents.map { it.id!!.value },
                )
            )
        }.toDomain()
    }
}
