package pl.edu.wat.wcy.epistimi.organization

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.teacher.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRepository

@Component
class OrganizationContextProvider(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val teacherRepository: TeacherRepository,
) {
    fun provide(userId: UserId): Organization? {
        return with(userRepository.findById(userId)) {
            when (role) {
                EPISTIMI_ADMIN -> null
                ORGANIZATION_ADMIN -> provideForOrganizationAdmin(userId)
                TEACHER -> provideForTeacher(userId)
                STUDENT -> provideForStudent(userId)
                PARENT -> provideForParent(userId)
            }
        }
    }

    private fun provideForOrganizationAdmin(userId: UserId): Organization? {
        return organizationRepository.findFirstByAdminId(userId)
    }

    private fun provideForTeacher(userId: UserId): Organization {
        return teacherRepository.findByUserId(userId).organizationId
            .let { organizationId -> organizationRepository.findById(organizationId) }
    }

    private fun provideForStudent(userId: UserId): Organization {
        TODO()
    }

    private fun provideForParent(userId: UserId): Organization {
        TODO()
    }
}
