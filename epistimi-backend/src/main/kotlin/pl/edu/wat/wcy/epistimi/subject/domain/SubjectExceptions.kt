package pl.edu.wat.wcy.epistimi.subject.domain

class SubjectBadRequestException(message: String) : RuntimeException(message)
class SubjectNotFoundException(subjectId: SubjectId) : RuntimeException("Subject with id [${subjectId.value}] was not found.")
