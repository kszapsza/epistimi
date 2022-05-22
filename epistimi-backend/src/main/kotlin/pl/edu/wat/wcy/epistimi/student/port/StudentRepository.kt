package pl.edu.wat.wcy.epistimi.student.port

import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.user.UserId

interface StudentRepository {
    fun findByUserId(id: UserId): Student
    fun findByIds(ids: List<StudentId>): List<Student>
    fun save(student: Student): Student
}
