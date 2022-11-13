package pl.edu.wat.wcy.epistimi.student.adapter.sql

import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.student.domain.StudentNotFoundException
import pl.edu.wat.wcy.epistimi.student.domain.port.StudentRepository
import pl.edu.wat.wcy.epistimi.user.domain.UserId

@Repository
class StudentDbRepository(
    private val studentJpaRepository: StudentJpaRepository,
) : StudentRepository {

    override fun findById(id: StudentId): Student {
        return studentJpaRepository.findById(id.value).orElseThrow { StudentNotFoundException() }
    }

    override fun findByUserId(id: UserId): Student {
        return studentJpaRepository.findFirstByUserId(id.value) ?: throw StudentNotFoundException()
    }

    override fun findByIds(ids: List<StudentId>): List<Student> {
        return studentJpaRepository.findAllById(ids.map { it.value })
    }

    override fun save(student: Student): Student {
        return studentJpaRepository.save(student)
    }
}
