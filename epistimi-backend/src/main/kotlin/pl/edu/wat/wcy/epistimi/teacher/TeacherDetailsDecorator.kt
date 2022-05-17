package pl.edu.wat.wcy.epistimi.teacher

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Component
class TeacherDetailsDecorator(
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository,
) {

    fun decorate(teacher: Teacher) = with(teacher) {
        TeacherDetails(
            id = id,
            user = userRepository.findById(userId),
            organization = organizationRepository.findById(organizationId),
            academicTitle = academicTitle,
        )
    }

}
