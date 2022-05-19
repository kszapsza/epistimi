package pl.edu.wat.wcy.epistimi.student.adapter.mongo

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.user.UserId

object StudentDbBiMapper : BiMapper<Student, StudentMongoDbDocument> {
    override fun toDomain(entity: StudentMongoDbDocument) = with(entity) {
        Student(
            id = StudentId(id!!),
            userId = UserId(userId),
            organizationId = OrganizationId(organizationId),
            parentsIds = parentIds.map { ParentId(it) }
        )
    }

    override fun fromDomain(domainObject: Student) = with(domainObject) {
        StudentMongoDbDocument(
            id = id?.value,
            userId = userId.value,
            organizationId = organizationId.value,
            parentIds = parentsIds.map { it.value },
        )
    }
}
