package pl.edu.wat.wcy.epistimi.grade.adapter.rest.mapper

import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategoriesResponse
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategoriesResponseEntry
import pl.edu.wat.wcy.epistimi.grade.adapter.rest.dto.GradeCategorySubjectResponse
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoriesForSubject
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.subject.domain.Subject

object GradeCategoriesResponseMapper : FromDomainMapper<GradeCategoriesForSubject, GradeCategoriesResponse> {
    override fun fromDomain(domainObject: GradeCategoriesForSubject) = with(domainObject) {
        GradeCategoriesResponse(
            subject = subject.toGradeCategorySubjectResponse(),
            categories = categories.map(GradeCategory::toGradeCategoriesResponseEntry),
        )
    }
}

private fun Subject.toGradeCategorySubjectResponse() =
    GradeCategorySubjectResponse(
        id = id!!.value.toString(),
        name = name,
    )

private fun GradeCategory.toGradeCategoriesResponseEntry() =
    GradeCategoriesResponseEntry(
        id = id!!.value.toString(),
        name = name,
        defaultWeight = defaultWeight,
        color = color,
    )
