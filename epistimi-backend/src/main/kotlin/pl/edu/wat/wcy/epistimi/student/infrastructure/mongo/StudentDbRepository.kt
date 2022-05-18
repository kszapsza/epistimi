package pl.edu.wat.wcy.epistimi.student.infrastructure.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.StudentRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class StudentDbRepository(
    private val studentMongoDbRepository: StudentMongoDbRepository,
) : StudentRepository {

    override fun findByIds(ids: List<StudentId>): List<Student> {
        return studentMongoDbRepository.findAllById(ids.map { it.value })
            .map { it.toDomain() }
            .toList()
    }

    private fun StudentMongoDbDocument.toDomain() = Student(
        id = StudentId(id!!),
        userId = UserId(userId),
        organizationId = OrganizationId(organizationId),
        parentsIds = parentIds.map { ParentId(it) }
    )

    override fun save(student: Student): Student {
        return student.run {
            studentMongoDbRepository.save(
                StudentMongoDbDocument(
                    id = null,
                    userId = userId.value,
                    organizationId = organizationId.value,
                    parentIds = parentsIds.map { it.value },
                )
            )
        }.toDomain()
    }
}
