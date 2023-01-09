package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoriesForSubject
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId
import pl.edu.wat.wcy.epistimi.grade.domain.request.GradeCategoryCreateRequest
import pl.edu.wat.wcy.epistimi.grade.domain.request.GradeCategoryUpdateRequest
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeCategoryService
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.User

class GradeCategoryFacade(
    private val gradeCategoryService: GradeCategoryService,
) {
    fun getCategoryById(
        contextUser: User,
        gradeCategoryId: GradeCategoryId,
    ): GradeCategory {
        return gradeCategoryService.getCategoryById(contextUser, gradeCategoryId)
    }

    fun getCategoriesForSubjectId(
        contextUser: User,
        subjectId: SubjectId,
    ): GradeCategoriesForSubject {
        return gradeCategoryService.getCategoriesForSubjectId(contextUser, subjectId)
    }

    fun createGradeCategory(
        contextUser: User,
        createRequest: GradeCategoryCreateRequest,
    ): GradeCategory {
        return gradeCategoryService.createGradeCategory(contextUser, createRequest)
    }

    fun updateGradeCategory(
        contextUser: User,
        updateRequest: GradeCategoryUpdateRequest,
    ): GradeCategory {
        return gradeCategoryService.updateGradeCategory(contextUser, updateRequest)
    }
}
