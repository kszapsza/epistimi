package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.course.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.teacher.Teacher

@Component
internal class CourseStubbing(
    private val courseRepository: CourseRepository,
) {
    fun courseExists(
        id: CourseId? = null,
        organization: Organization,
        code: Course.Code,
        schoolYear: String,
        classTeacher: Teacher,
        students: List<Student>,
    ): Course {
        return courseRepository.save(
            Course(
                id = id,
                organization = organization,
                code = code,
                schoolYear = schoolYear,
                classTeacher = classTeacher,
                students = students,
            )
        )
    }
}
