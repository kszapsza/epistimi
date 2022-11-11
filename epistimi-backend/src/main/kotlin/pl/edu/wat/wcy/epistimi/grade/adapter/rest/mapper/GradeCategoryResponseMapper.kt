package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategoryResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategorySubjectResponse
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.subject.domain.Subject

object GradeCategoryResponseMapper : FromDomainMapper<GradeCategory, GradeCategoryResponse> {
    override fun fromDomain(domainObject: GradeCategory): GradeCategoryResponse {
        return domainObject.toGradeCategoryResponse()
    }
}

private fun GradeCategory.toGradeCategoryResponse() =
    GradeCategoryResponse(
        id = id!!.value.toString(),
        subject = subject.toGradeCategorySubjectResponse(),
        name = name,
        defaultWeight = defaultWeight,
        color = color,
    )

private fun Subject.toGradeCategorySubjectResponse() =
    GradeCategorySubjectResponse(
        id = id!!.value.toString(),
        name = name,
    )
