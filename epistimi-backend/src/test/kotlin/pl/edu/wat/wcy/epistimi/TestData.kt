package pl.edu.wat.wcy.epistimi

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.parent.domain.ParentId
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserId
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.PARENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.STUDENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.TEACHER
import pl.edu.wat.wcy.epistimi.user.domain.UserSex
import pl.edu.wat.wcy.epistimi.user.domain.UserSex.MALE
import java.time.LocalDate
import java.util.UUID

internal object TestData {
    val address = Address(
        street = "Szkolna 17",
        postalCode = "15-640",
        city = "Białystok",
    )

    fun organization(
        id: OrganizationId = OrganizationId(UUID.randomUUID()),
        name: String = "SP7",
        admins: Set<User> = emptySet(),
        street: String = address.street,
        city: String = address.city,
        postalCode: String = address.postalCode,
        latitude: Double? = null,
        longitude: Double? = null,
    ) = Organization(
        id = id,
        name = name,
        admins = admins,
        street = street,
        city = city,
        postalCode = postalCode,
        latitude = latitude,
        longitude = longitude,
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
                organization = organization(),
                pesel = null,
                email = null,
                phoneNumber = null,
                street = null,
                postalCode = null,
                city = null,
            )
        }

        fun epistimiAdmin(
            id: UserId = UserId(UUID.randomUUID()),
            firstName: String = "Jan",
            lastName: String = "Kowalski",
            username: String = "epistimi_admin",
            passwordHash: String = "123456",
            sex: UserSex = MALE,
            organization: Organization = organization(),
            pesel: String? = null,
            email: String? = null,
            phoneNumber: String? = null,
            street: String? = null,
            postalCode: String? = null,
            city: String? = null,
        ) = User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            role = EPISTIMI_ADMIN,
            username = username,
            passwordHash = passwordHash,
            sex = sex,
            organization = organization,
            pesel = pesel,
            email = email,
            phoneNumber = phoneNumber,
            street = street,
            postalCode = postalCode,
            city = city,
        )

        fun organizationAdmin(
            id: UserId = UserId(UUID.randomUUID()),
            firstName: String = "Jan",
            lastName: String = "Kowalski",
            username: String = "organization_admin",
            passwordHash: String = "123456",
            sex: UserSex = MALE,
            organization: Organization = organization(),
            pesel: String? = null,
            email: String? = null,
            phoneNumber: String? = null,
            street: String? = null,
            postalCode: String? = null,
            city: String? = null,
        ) = User(
            id = id,
            organization = organization,
            firstName = firstName,
            lastName = lastName,
            role = ORGANIZATION_ADMIN,
            username = username,
            passwordHash = passwordHash,
            pesel = pesel,
            sex = sex,
            email = email,
            phoneNumber = phoneNumber,
            street = street,
            postalCode = postalCode,
            city = city
        )

        fun teacher(
            id: UserId = UserId(UUID.randomUUID()),
            firstName: String = "Jan",
            lastName: String = "Kowalski",
            username: String = "teacher",
            passwordHash: String = "123456",
            sex: UserSex = MALE,
            organization: Organization = organization(),
            pesel: String? = null,
            email: String? = null,
            phoneNumber: String? = null,
            street: String? = null,
            postalCode: String? = null,
            city: String? = null,
        ) = User(
            id = id,
            organization = organization,
            firstName = firstName,
            lastName = lastName,
            role = TEACHER,
            username = username,
            passwordHash = passwordHash,
            pesel = pesel,
            sex = sex,
            email = email,
            phoneNumber = phoneNumber,
            street = street,
            postalCode = postalCode,
            city = city
        )

        fun student(
            id: UserId = UserId(UUID.randomUUID()),
            firstName: String = "Jan",
            lastName: String = "Kowalski",
            username: String = "student",
            passwordHash: String = "123456",
            sex: UserSex = MALE,
            organization: Organization = organization(),
            pesel: String? = null,
            email: String? = null,
            phoneNumber: String? = null,
            street: String? = null,
            postalCode: String? = null,
            city: String? = null,
        ) = User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            role = STUDENT,
            username = username,
            passwordHash = passwordHash,
            sex = sex,
            organization = organization,
            pesel = pesel,
            email = email,
            phoneNumber = phoneNumber,
            street = street,
            postalCode = postalCode,
            city = city,
        )

        fun parent(
            id: UserId = UserId(UUID.randomUUID()),
            firstName: String = "Jan",
            lastName: String = "Kowalski",
            username: String = "parent",
            passwordHash: String = "123456",
            sex: UserSex = MALE,
            organization: Organization = organization(),
            pesel: String? = null,
            email: String? = null,
            phoneNumber: String? = null,
            street: String? = null,
            postalCode: String? = null,
            city: String? = null,
        ) = User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            role = PARENT,
            username = username,
            passwordHash = passwordHash,
            sex = sex,
            organization = organization,
            pesel = pesel,
            email = email,
            phoneNumber = phoneNumber,
            street = street,
            postalCode = postalCode,
            city = city,
        )
    }

    fun teacher(
        id: TeacherId = TeacherId(UUID.randomUUID()),
        user: User = Users.teacher(),
        academicTitle: String? = null,
    ) = Teacher(
        id = id,
        user = user,
        academicTitle = academicTitle,
    )

    fun parent(
        id: ParentId = ParentId(UUID.randomUUID()),
        user: User = Users.parent()
    ) = Parent(
        id = id,
        user = user,
    )

    fun course(
        id: CourseId = CourseId(UUID.randomUUID()),
        organization: Organization = organization(),
        codeNumber: Int = 6,
        codeLetter: String = "a",
        classTeacher: Teacher = teacher(),
        students: Set<Student> = emptySet(),
        schoolYearBegin: LocalDate = TestUtils.parseDate("2012-09-03"),
        schoolYearSemesterEnd: LocalDate = TestUtils.parseDate("2013-01-18"),
        schoolYearEnd: LocalDate = TestUtils.parseDate("2013-06-28"),
        profession: String? = null,
        profile: String? = null,
        specialization: String? = null,
        subjects: Set<Subject> = emptySet(),
    ) = Course(
        id = id,
        organization = organization,
        codeNumber = codeNumber,
        codeLetter = codeLetter,
        classTeacher = classTeacher,
        students = students,
        schoolYearBegin = schoolYearBegin,
        schoolYearSemesterEnd = schoolYearSemesterEnd,
        schoolYearEnd = schoolYearEnd,
        profession = profession,
        profile = profile,
        specialization = specialization,
        subjects = subjects,
    )

    fun student(
        id: StudentId = StudentId(UUID.randomUUID()),
        user: User = Users.student(),
        parents: List<Parent> = emptyList(),
        course: Course = course(),
    ) = Student(
        id = id,
        user = user,
        parents = parents,
        course = course,
    )

    fun subject(
        id: SubjectId = SubjectId(UUID.randomUUID()),
        name: String = "Język polski",
        course: Course = course(),
        teacher: Teacher = teacher(),
    ) = Subject(
        id = id,
        name = name,
        course = course,
        teacher = teacher,
    )
}
