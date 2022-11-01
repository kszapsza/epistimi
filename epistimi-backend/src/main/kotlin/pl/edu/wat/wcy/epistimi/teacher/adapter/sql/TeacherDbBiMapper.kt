package pl.edu.wat.wcy.epistimi.teacher.adapter.sql

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.teacher.Teacher
import pl.edu.wat.wcy.epistimi.teacher.TeacherId
import pl.edu.wat.wcy.epistimi.user.adapter.sql.UserDbBiMapper

object TeacherDbBiMapper : BiMapper<Teacher, TeacherJpaEntity> {
    override fun toDomain(entity: TeacherJpaEntity) = with(entity) {
        Teacher(
            id = TeacherId(id!!),
            user = UserDbBiMapper.toDomain(user),
            academicTitle = academicTitle,
        )
    }

    override fun fromDomain(domainObject: Teacher) = with(domainObject) {
        TeacherJpaEntity(
            id = id?.value,
            user = UserDbBiMapper.fromDomain(user),
            academicTitle = academicTitle,
        )
    }
}
