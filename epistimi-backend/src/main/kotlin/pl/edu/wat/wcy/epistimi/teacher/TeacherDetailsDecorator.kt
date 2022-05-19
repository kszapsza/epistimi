package pl.edu.wat.wcy.epistimi.teacher

import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

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
