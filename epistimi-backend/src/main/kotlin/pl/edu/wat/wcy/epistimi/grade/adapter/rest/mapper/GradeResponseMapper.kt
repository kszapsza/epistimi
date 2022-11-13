package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategorySimpleResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeTeacherResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeValueResponse
import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeValue
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher

object GradeResponseMapper : FromDomainMapper<Grade, GradeResponse> {
    override fun fromDomain(domainObject: Grade) =
        domainObject.toGradeResponse()
}

private fun Grade.toGradeResponse() =
    GradeResponse(
        id = id!!.value.toString(),
        issuedBy = issuedBy.toGradeTeacherResponse(),
        category = category.toGradeCategoryResponse(),
        semester = semester,
        issuedAt = issuedAt!!,
        updatedAt = updatedAt,
        value = value.toGradeValueResponse(),
        weight = weight,
        countTowardsAverage = countTowardsAverage,
        comment = comment,
    )

private fun Teacher.toGradeTeacherResponse() =
    GradeTeacherResponse(
        id = id!!.value.toString(),
        academicTitle = academicTitle,
        firstName = user.firstName,
        lastName = user.lastName,
    )

private fun GradeCategory.toGradeCategoryResponse() =
    GradeCategorySimpleResponse(
        id = id!!.value.toString(),
        name = name,
        color = color,
    )

private fun GradeValue.toGradeValueResponse() =
    GradeValueResponse(
        displayName = displayName,
        fullName = fullName,
    )
