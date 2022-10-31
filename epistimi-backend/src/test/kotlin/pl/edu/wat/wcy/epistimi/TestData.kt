package pl.edu.wat.wcy.epistimi

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.ENABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.subject.Subject
import pl.edu.wat.wcy.epistimi.subject.SubjectId
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

    internal object Users {
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
            id: UUID,
            username: String? = null,
        ): User {
            val roleName = role.toString().lowercase()
            return baseUser.copy(
                id = UserId(id),
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

    val student = Student(
        id = StudentId(UUID.randomUUID()),
        user = Users.student,
        organization = organization,
        parents = emptyList(),
    )

    val parent = Parent(
        id = ParentId(UUID.randomUUID()),
        user = Users.parent,
        organization = organization,
    )

    val course = Course(
        id = CourseId(UUID.randomUUID()),
        organization = organization,
        code = Course.Code(number = 6, letter = "a"),
        schoolYear = "2012/2013",
        classTeacher = teacher,
        students = listOf(student),
        schoolYearBegin = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd = TestUtils.parseDate("2013-06-28"),
        profession = null,
        profile = null,
        specialization = null,
    )

    val subject = Subject(
        id = SubjectId(UUID.randomUUID()),
        name = "Język polski",
        course = course,
        teacher = teacher,
    )
}
