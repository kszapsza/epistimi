package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.User

@Component
class TeacherStubbing(
    private val teacherRepository: TeacherRepository,
) {
    fun teacherExists(
        id: TeacherId? = null,
        user: User,
        organization: Organization,
        academicTitle: String? = "dr",
    ): Teacher {
        return teacherRepository.save(
            Teacher(
                id = id,
                user = user,
                organization = organization,
                academicTitle = academicTitle,
            )
        )
    }
}
