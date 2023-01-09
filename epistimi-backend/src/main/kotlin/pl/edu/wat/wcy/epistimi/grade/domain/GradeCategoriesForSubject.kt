package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.subject.domain.Subject

data class GradeCategoriesForSubject(
    val subject: Subject,
    val categories: List<GradeCategory>,
)
