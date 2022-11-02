package pl.edu.wat.wcy.epistimi.subject

class SubjectBadRequestException(message: String) : RuntimeException(message)
class SubjectNotFoundException(subjectId: SubjectId): RuntimeException("Subject with id [${subjectId.value}] was not found.")
