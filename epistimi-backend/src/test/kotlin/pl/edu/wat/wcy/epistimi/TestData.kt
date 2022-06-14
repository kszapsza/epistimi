package pl.edu.wat.wcy.epistimi

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import java.util.UUID

internal object TestData {
    val address = Address(
        street = "Szkolna 17",
        postalCode = "15-640",
        city = "Białystok",
    )

    val organization = Organization(
        id = OrganizationId(UUID.randomUUID()),
        name = "SP7",
        admin = Users.organizationAdmin,
        status = ENABLED,
        address = address,
        location = null,
    )

    object Users {
        private val baseUser = User(
            id = UserId(UUID.randomUUID()),
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
            id = UserId(UUID.randomUUID()),
            role = EPISTIMI_ADMIN,
            username = "epistimi_admin",
        )

        val organizationAdmin = baseUser.copy(
            id = UserId(UUID.randomUUID()),
            role = ORGANIZATION_ADMIN,
            username = "organization_admin",
        )

        val teacher = baseUser.copy(
            id = UserId(UUID.randomUUID()),
            role = TEACHER,
            username = "teacher",
        )

        val student = baseUser.copy(
            id = UserId(UUID.randomUUID()),
            role = STUDENT,
            username = "student",
        )

        val parent = baseUser.copy(
            id = UserId(UUID.randomUUID()),
            role = PARENT,
            username = "parent",
        )
    }

    val teacher = Teacher(
        id = TeacherId(UUID.randomUUID()),
        user = Users.teacher,
        organization = organization,
        academicTitle = null,
    )
}
