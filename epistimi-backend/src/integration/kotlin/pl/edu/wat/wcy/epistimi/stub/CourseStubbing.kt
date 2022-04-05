package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Component
internal class CourseStubbing(
    private val courseRepository: CourseRepository,
) {
    fun courseExists(
        id: CourseId? = null,
        organization: Organization,
        code: Course.Code = Course.Code(number = "6", letter = "a"),
        schoolYear: String = "2012/2013",
        classTeacher: Teacher,
        students: List<Student> = listOf(),
        schoolYearBegin: Date = DATE_FORMAT.parse("2012-09-03"),
        schoolYearSemesterEnd: Date = DATE_FORMAT.parse("2013-01-18"),
        schoolYearEnd: Date = DATE_FORMAT.parse("2013-06-28"),
        profile: String? = "matematyczno-fizyczny",
        profession: String? = "technik informatyk",
        specialization: String? = "zarządzanie projektami w IT",
    ): Course {
        return courseRepository.save(
            Course(
                id = id,
                organization = organization,
                code = code,
                schoolYear = schoolYear,
                classTeacher = classTeacher,
                students = students,
                schoolYearBegin = schoolYearBegin,
                schoolYearSemesterEnd = schoolYearSemesterEnd,
                schoolYearEnd = schoolYearEnd,
                profile = profile,
                profession = profession,
                specialization = specialization,
            )
        )
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }
}
