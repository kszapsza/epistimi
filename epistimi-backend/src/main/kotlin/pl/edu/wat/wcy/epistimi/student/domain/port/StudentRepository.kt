package pl.edu.wat.wcy.epistimi.student.domain.port

import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.user.domain.UserId

interface StudentRepository {
    fun findById(id: StudentId): Student
    fun findByUserId(id: UserId): Student
    fun findByIds(ids: List<StudentId>): List<Student>
    fun save(student: Student): Student
}
