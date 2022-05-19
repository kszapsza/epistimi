package pl.edu.wat.wcy.epistimi.course.adapter.mongo

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.course.Course
import pl.edu.wat.wcy.epistimi.course.CourseId
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.teacher.TeacherId

object CourseDbBiMapper : BiMapper<Course, CourseMongoDbDocument> {
    override fun toDomain(entity: CourseMongoDbDocument) = with(entity) {
        Course(
            id = CourseId(id!!),
            organizationId = OrganizationId(organizationId),
            code = Course.Code(number = code.number, letter = code.letter),
            schoolYear = schoolYear,
            classTeacherId = TeacherId(classTeacherId),
            studentIds = studentIds.map { StudentId(it) },
            schoolYearBegin = schoolYearBegin,
            schoolYearSemesterEnd = schoolYearSemesterEnd,
            schoolYearEnd = schoolYearEnd,
            profile = profile,
            profession = profession,
            specialization = specialization,
        )
    }

    override fun fromDomain(domainObject: Course) = with(domainObject) {
        CourseMongoDbDocument(
            id = id?.value,
            organizationId = organizationId.value,
            code = CourseMongoDbDocument.Code(number = code.number, letter = code.letter),
            schoolYear = schoolYear,
            classTeacherId = classTeacherId.value,
            studentIds = studentIds.map { it.value },
            schoolYearBegin = schoolYearBegin,
            schoolYearSemesterEnd = schoolYearSemesterEnd,
            schoolYearEnd = schoolYearEnd,
            profile = profile,
            profession = profession,
            specialization = specialization,
        )
    }
}
