package pl.edu.wat.wcy.epistimi.student.port

import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId

interface StudentRepository {
    fun findByIds(ids: List<StudentId>): List<Student>
    fun save(student: Student): Student
}
