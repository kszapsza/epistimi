package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId

data class GradeFilters(
    val subjectIds: List<SubjectId>?,
    val studentIds: List<StudentId>?,
) {
    init {
        if (subjectIds.isNullOrEmpty() && studentIds.isNullOrEmpty()) {
            throw GradeIllegalFiltersException()
        }
    }
}
