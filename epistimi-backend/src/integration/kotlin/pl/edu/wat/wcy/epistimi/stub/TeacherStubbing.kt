package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.domain.User

@Component
internal class TeacherStubbing(
    private val teacherRepository: TeacherRepository,
) {
    fun teacherExists(
        id: TeacherId? = null,
        user: User,
        academicTitle: String? = "dr",
    ): Teacher {
        return teacherRepository.save(
            Teacher(
                id = id,
                user = user,
                academicTitle = academicTitle,
            )
        )
    }
}
