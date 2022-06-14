package pl.edu.wat.wcy.epistimi.course.adapter.sql

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationDbBiMapper
import pl.edu.wat.wcy.epistimi.student.adapter.sql.StudentDbBiMapper
import pl.edu.wat.wcy.epistimi.teacher.adapter.sql.TeacherDbBiMapper

object CourseDbBiMapper : BiMapper<Course, CourseJpaEntity> {
    override fun toDomain(entity: CourseJpaEntity) = with(entity) {
        Course(
            id = CourseId(id!!),
            organization = OrganizationDbBiMapper.toDomain(organization),
            code = Course.Code(number = codeNumber, letter = codeLetter),
            schoolYear = schoolYear,
            classTeacher = TeacherDbBiMapper.toDomain(classTeacher),
            students = students.map { StudentDbBiMapper.toDomain(it) },
            schoolYearBegin = schoolYearBegin,
            schoolYearSemesterEnd = schoolYearSemesterEnd,
            schoolYearEnd = schoolYearEnd,
            profile = profile,
            profession = profession,
            specialization = specialization,
        )
    }

    override fun fromDomain(domainObject: Course) = with(domainObject) {
        CourseJpaEntity(
            id = id?.value,
            organization = OrganizationDbBiMapper.fromDomain(organization),
            codeNumber = code.number,
            codeLetter = code.letter,
            schoolYear = schoolYear,
            classTeacher = TeacherDbBiMapper.fromDomain(classTeacher),
            students = students.map { StudentDbBiMapper.fromDomain(it) },
            schoolYearBegin = schoolYearBegin,
            schoolYearSemesterEnd = schoolYearSemesterEnd,
            schoolYearEnd = schoolYearEnd,
            profile = profile,
            profession = profession,
            specialization = specialization,
        )
    }
}
