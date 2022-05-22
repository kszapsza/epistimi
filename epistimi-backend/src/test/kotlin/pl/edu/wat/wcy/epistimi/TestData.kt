package pl.edu.wat.wcy.epistimi

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationDetails
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetails
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId

internal object TestData {
    val address = Address(
        street = "Szkolna 17",
        postalCode = "15-640",
        city = "Białystok",
    )

    val organization = Organization(
        id = OrganizationId("organization_id"),
        name = "SP7",
        adminId = Users.organizationAdmin.id!!,
        status = ENABLED,
        address = address,
        location = null,
    )

    val organizationDetails = OrganizationDetails(
        id = OrganizationId("organization_id"),
        name = "SP7",
        admin = Users.organizationAdmin,
        status = ENABLED,
        address = address,
        location = null,
    )

    object Users {
        private val baseUser = User(
            id = UserId("user_id"),
            firstName = "Jan",
            lastName = "Kowalski",
            role = EPISTIMI_ADMIN,
            username = "j.kowalski",
            passwordHash = "123456",
            sex = MALE,
        )

        fun withRole(
            role: User.Role,
            id: String? = null,
            username: String? = null,
        ): User {
            val roleName = role.toString().lowercase()
            return baseUser.copy(
                id = id?.let { UserId(id) } ?: UserId("${roleName}_user_id"),
                role = role,
                username = username ?: roleName,
            )
        }

        val epistimiAdmin = baseUser.copy(
            id = UserId("epistimi_admin_user_id"),
            role = EPISTIMI_ADMIN,
            username = "epistimi_admin",
        )

        val organizationAdmin = baseUser.copy(
            id = UserId("organization_admin_user_id"),
            role = ORGANIZATION_ADMIN,
            username = "organization_admin",
        )

        val teacher = baseUser.copy(
            id = UserId("teacher_user_id"),
            role = TEACHER,
            username = "teacher",
        )

        val student = baseUser.copy(
            id = UserId("student_user_id"),
            role = STUDENT,
            username = "student",
        )

        val parent = baseUser.copy(
            id = UserId("parent_user_id"),
            role = PARENT,
            username = "parent",
        )
    }

    val teacher = Teacher(
        id = TeacherId("teacher_id"),
        userId = Users.teacher.id!!,
        organizationId = organization.id!!,
        academicTitle = null,
    )

    val teacherDetails = TeacherDetails(
        id = TeacherId("teacher_id"),
        user = Users.teacher,
        organization = organization,
        academicTitle = null,
    )
}
