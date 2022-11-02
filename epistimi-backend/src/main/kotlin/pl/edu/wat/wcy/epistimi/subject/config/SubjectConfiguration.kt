package pl.edu.wat.wcy.epistimi.subject.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.subject.SubjectService
import pl.edu.wat.wcy.epistimi.subject.port.SubjectRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository

@Configuration
class SubjectConfiguration {

    @Bean
    fun subjectService(
        subjectRepository: SubjectRepository,
        courseRepository: CourseRepository,
        teacherRepository: TeacherRepository,
    ): SubjectService {
        return SubjectService(
            subjectRepository,
            courseRepository,
            teacherRepository,
        )
    }

}
