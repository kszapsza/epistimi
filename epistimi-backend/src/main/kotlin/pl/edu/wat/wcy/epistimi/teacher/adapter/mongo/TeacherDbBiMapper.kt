package pl.edu.wat.wcy.epistimi.teacher.adapter.mongo

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.UserId

object TeacherDbBiMapper : BiMapper<Teacher, TeacherMongoDbDocument> {
    override fun toDomain(entity: TeacherMongoDbDocument) = with(entity) {
        Teacher(
            id = TeacherId(id!!),
            userId = UserId(userId),
            organizationId = OrganizationId(organizationId),
            academicTitle = academicTitle,
        )
    }

    override fun fromDomain(domainObject: Teacher) = with(domainObject) {
        TeacherMongoDbDocument(
            id = null,
            userId = userId.value,
            organizationId = organizationId.value,
            academicTitle = academicTitle,
        )
    }
}
