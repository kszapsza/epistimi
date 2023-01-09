package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.course.domain.Course
import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.course.domain.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Component
internal class CourseStubbing(
    private val courseRepository: CourseRepository,
) {
    companion object {
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    }

    fun courseExists(
        id: CourseId? = null,
        organization: Organization,
        codeNumber: Int = 6,
        codeLetter: String = "a",
        schoolYear: String = "2012/2013",
        classTeacher: Teacher,
        students: Set<Student> = emptySet(),
        schoolYearBegin: LocalDate = LocalDate.parse("2012-09-03", DATE_FORMAT),
        schoolYearSemesterEnd: LocalDate = LocalDate.parse("2013-01-18"),
        schoolYearEnd: LocalDate = LocalDate.parse("2013-06-28"),
        profile: String? = "matematyczno-fizyczny",
        profession: String? = "technik informatyk",
        specialization: String? = "zarządzanie projektami w IT",
        subjects: Set<Subject> = emptySet(),
    ): Course {
        return courseRepository.save(
            Course(
                id = id,
                organization = organization,
                codeNumber = codeNumber,
                codeLetter = codeLetter,
                classTeacher = classTeacher,
                students = students,
                schoolYearBegin = schoolYearBegin,
                schoolYearSemesterEnd = schoolYearSemesterEnd,
                schoolYearEnd = schoolYearEnd,
                profile = profile,
                profession = profession,
                specialization = specialization,
                subjects = subjects,
            )
        )
    }
}
