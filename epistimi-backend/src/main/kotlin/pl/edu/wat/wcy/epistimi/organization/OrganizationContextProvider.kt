package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

class OrganizationContextProvider(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository,
    private val parentRepository: ParentRepository,
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
        return teacherRepository.findByUserId(userId).organization
    }

    private fun provideForStudent(userId: UserId): Organization {
        return studentRepository.findByUserId(userId).organization
    }

    private fun provideForParent(userId: UserId): Organization {
        return parentRepository.findByUserId(userId).organization
    }
}
