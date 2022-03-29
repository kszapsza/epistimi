package pl.edu.wat.wcy.epistimi.teacher

interface TeacherRepository {
    fun findById(id: TeacherId): Teacher
}
