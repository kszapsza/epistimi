package pl.edu.wat.wcy.epistimi.student

interface StudentRepository {
    fun findByIds(ids: List<StudentId>): List<Student>
    fun save(student: Student): Student
}
