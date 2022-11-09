package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.Grade
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategoryResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeTeacherResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeValueResponse

object GradeResponseMapper : FromDomainMapper<Grade, GradeResponse> {
    override fun fromDomain(domainObject: Grade): GradeResponse {
        return domainObject.toGradeResponse()
    }

    private fun Grade.toGradeResponse(): GradeResponse {
        return GradeResponse(
            id = id!!.value.toString(),
            issuedBy = GradeTeacherResponse(
                id = issuedBy.id!!.value.toString(),
                academicTitle = issuedBy.academicTitle,
                firstName = issuedBy.user.firstName,
                lastName = issuedBy.user.lastName,
            ),
            category = GradeCategoryResponse(
                id = category.id!!.value.toString(),
                name = category.name,
                color = category.color,
            ),
            semester = semester,
            issuedAt = issuedAt!!,
            updatedAt = updatedAt,
            value = GradeValueResponse(
                displayName = value.displayName,
                fullName = value.fullName,
            ),
            weight = weight,
            countTowardsAverage = countTowardsAverage,
            comment = comment,
        )
    }
}
