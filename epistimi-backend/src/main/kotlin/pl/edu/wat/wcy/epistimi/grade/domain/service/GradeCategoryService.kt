package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoriesForSubject
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategory
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryActionForbiddenException
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryCreateRequest
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategorySubjectNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeCategoryAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeCategoryRepository
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectNotFoundException
import pl.edu.wat.wcy.epistimi.user.domain.User

class GradeCategoryService(
    private val gradeCategoryRepository: GradeCategoryRepository,
    private val gradeCategoryAccessValidator: GradeCategoryAccessValidator,
    private val subjectFacade: SubjectFacade,
) {
    fun getCategoryById(
        contextUser: User,
        gradeCategoryId: GradeCategoryId,
    ): GradeCategory {
        return gradeCategoryRepository.findById(gradeCategoryId)
            .also { gradeCategory ->
                if (!gradeCategoryAccessValidator.canRetrieve(contextUser, gradeCategory)) {
                    throw GradeCategoryNotFoundException(gradeCategoryId)
                }
            }
    }

    fun getCategoriesForSubjectId(
        contextUser: User,
        subjectId: SubjectId,
    ): GradeCategoriesForSubject {
        return GradeCategoriesForSubject(
            subject = findSubject(contextUser, subjectId),
            categories = gradeCategoryRepository.findAllBySubjectId(subjectId),
        )
    }

    private fun findSubject(
        contextUser: User,
        subjectId: SubjectId,
    ): Subject {
        return try {
            subjectFacade.getSubject(contextUser, subjectId)
        } catch (ex: SubjectNotFoundException) {
            throw GradeCategorySubjectNotFoundException(subjectId)
        }
    }

    fun createGradeCategory(
        contextUser: User,
        createRequest: GradeCategoryCreateRequest,
    ): GradeCategory {
        val (subjectId, name, defaultWeight, color) = createRequest
        val subject = findSubject(contextUser = contextUser, subjectId = subjectId)
        val gradeCategory = GradeCategory(id = null, subject, name, defaultWeight, color)

        return if (gradeCategoryAccessValidator.canCreate(contextUser, gradeCategory)) {
            gradeCategoryRepository.save(gradeCategory)
        } else {
            throw GradeCategoryActionForbiddenException(
                "Insufficient permissions to create grade category for subject with id [$subjectId]"
            )
        }
    }
}
