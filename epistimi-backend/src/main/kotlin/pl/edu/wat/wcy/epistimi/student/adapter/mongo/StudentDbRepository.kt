package pl.edu.wat.wcy.epistimi.student.adapter.mongo

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.common.mapper.DbHandlers
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.student.StudentNotFoundException
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.user.UserId

@Repository
class StudentDbRepository(
    private val studentMongoDbRepository: StudentMongoDbRepository,
) : StudentRepository {

    override fun findByUserId(id: UserId): Student {
        return DbHandlers.handleDbGet(mapper = StudentDbBiMapper) {
            studentMongoDbRepository.findFirstByUserId(id.value)
                ?: throw StudentNotFoundException()
        }
    }

    override fun findByIds(ids: List<StudentId>): List<Student> {
        return DbHandlers.handleDbMultiGet(mapper = StudentDbBiMapper) {
            studentMongoDbRepository.findAllById(ids.map { it.value })
        }
    }

    override fun save(student: Student): Student {
        return DbHandlers.handleDbInsert(
            mapper = StudentDbBiMapper,
            domainObject = student,
            dbCall = studentMongoDbRepository::save,
        )
    }
}
