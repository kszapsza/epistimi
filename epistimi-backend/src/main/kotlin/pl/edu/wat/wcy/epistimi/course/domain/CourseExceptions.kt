package pl.edu.wat.wcy.epistimi.course.domain

class CourseNotFoundException(id: CourseId) : Exception("Course with id ${id.value} not found")
class CourseBadRequestException(cause: String) : Exception(cause)
class CourseUnmodifiableException : Exception("Cannot modify course which is ended")
