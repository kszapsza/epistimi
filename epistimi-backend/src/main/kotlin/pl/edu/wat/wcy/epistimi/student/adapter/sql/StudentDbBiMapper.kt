package pl.edu.wat.wcy.epistimi.student.adapter.sql

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationDbBiMapper
import pl.edu.wat.wcy.epistimi.parent.adapter.sql.ParentDbBiMapper
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserDbBiMapper

object StudentDbBiMapper : BiMapper<Student, StudentJpaEntity> {
    override fun toDomain(entity: StudentJpaEntity) = with(entity) {
        Student(
            id = StudentId(id!!),
            user = UserDbBiMapper.toDomain(user),
            organization = OrganizationDbBiMapper.toDomain(organization),
            parents = parents.map { ParentDbBiMapper.toDomain(it) }
        )
    }

    override fun fromDomain(domainObject: Student) = with(domainObject) {
        StudentJpaEntity(
            id = id?.value,
            user = UserDbBiMapper.fromDomain(user),
            organization = OrganizationDbBiMapper.fromDomain(organization),
            parents = parents.map { ParentDbBiMapper.fromDomain(it) },
        )
    }
}
