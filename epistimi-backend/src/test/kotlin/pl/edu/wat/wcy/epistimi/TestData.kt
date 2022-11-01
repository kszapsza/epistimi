package pl.edu.wat.wcy.epistimi

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationStatus.ENABLED
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
import pl.edu.wat.wcy.epistimi.user.UserRole.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.UserRole.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.UserRole.PARENT
import pl.edu.wat.wcy.epistimi.user.UserRole.STUDENT
import pl.edu.wat.wcy.epistimi.user.UserRole.TEACHER
import pl.edu.wat.wcy.epistimi.user.UserSex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserRole
import java.util.UUID

internal object TestData {
    val address = Address(
        street = "Szkolna 17",
        postalCode = "15-640",
        city = "Białystok",
    )

    val organization: Organization = Organization(
        id = OrganizationId(UUID.randomUUID()),
        name = "SP7",
        admin = Users.organizationAdmin,
        status = ENABLED,
        street = address.street,
        city = address.city,
        postalCode = address.postalCode,
        latitude = null,
        longitude = null,
    )

    internal object Users {
        fun withRole(
            role: UserRole,
            id: UUID,
            username: String? = null,
        ): User {
            val roleName = role.toString().lowercase()
            return User(
                id = UserId(id),
                firstName = "Jan",
                lastName = "Kowalski",
                role = role,
                username = username ?: roleName,
                passwordHash = "123456",
                sex = MALE,
                organization = organization,
                pesel = null,
                email = null,
                phoneNumber = null,
                street = null,
                postalCode = null,
                city = null,
            )
        }

        val epistimiAdmin = User(
            id = UserId(UUID.randomUUID()),
            firstName = "Jan",
            lastName = "Kowalski",
            role = EPISTIMI_ADMIN,
            username = "epistimi_admin",
            passwordHash = "123456",
            sex = MALE,
            organization = organization,
            pesel = null,
            email = null,
            phoneNumber = null,
            street = null,
            postalCode = null,
            city = null,
        )

        val organizationAdmin = User(
            id = UserId(UUID.randomUUID()),
            firstName = "Jan",
            lastName = "Kowalski",
            role = ORGANIZATION_ADMIN,
            username = "organization_admin",
            passwordHash = "123456",
            sex = MALE,
            organization = organization,
            pesel = null,
            email = null,
            phoneNumber = null,
            street = null,
            postalCode = null,
            city = null,
        )

        val teacher = User(
            id = UserId(UUID.randomUUID()),
            firstName = "Jan",
            lastName = "Kowalski",
            role = TEACHER,
            username = "teacher",
            passwordHash = "123456",
            sex = MALE,
            organization = organization,
            pesel = null,
            email = null,
            phoneNumber = null,
            street = null,
            postalCode = null,
            city = null,
        )

        val student = User(
            id = UserId(UUID.randomUUID()),
            firstName = "Jan",
            lastName = "Kowalski",
            role = STUDENT,
            username = "student",
            passwordHash = "123456",
            sex = MALE,
            organization = organization,
            pesel = null,
            email = null,
            phoneNumber = null,
            street = null,
            postalCode = null,
            city = null,
        )

        val parent = User(
            id = UserId(UUID.randomUUID()),
            firstName = "Jan",
            lastName = "Kowalski",
            role = PARENT,
            username = "parent",
            passwordHash = "123456",
            sex = MALE,
            organization = organization,
            pesel = null,
            email = null,
            phoneNumber = null,
            street = null,
            postalCode = null,
            city = null,
        )
    }

    val teacher = Teacher(
        id = TeacherId(UUID.randomUUID()),
        user = Users.teacher,
        academicTitle = null,
    )

    val student = Student(
        id = StudentId(UUID.randomUUID()),
        user = Users.student,
        parents = emptyList(),
    )

    val parent = Parent(
        id = ParentId(UUID.randomUUID()),
        user = Users.parent,
    )

    val course = Course(
        id = CourseId(UUID.randomUUID()),
        organization = organization,
        codeNumber = 6,
        codeLetter = "a",
        classTeacher = teacher,
        students = setOf(student),
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
