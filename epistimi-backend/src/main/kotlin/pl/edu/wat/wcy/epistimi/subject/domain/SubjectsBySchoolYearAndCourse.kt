package pl.edu.wat.wcy.epistimi.subject.domain

import pl.edu.wat.wcy.epistimi.course.domain.CourseId
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher

data class SubjectsBySchoolYearAndCourse(
    val schoolYears: List<SchoolYearSubjects>,
)

data class SchoolYearSubjects(
    val schoolYear: String,
    val courses: List<CourseSubjects>
)

data class CourseSubjects(
    val id: CourseId,
    val codeNumber: Int,
    val codeLetter: String,
    val schoolYear: String,
    val classTeacher: Teacher,
    val students: List<Student>,
    val subjects: List<Subject>,
)
