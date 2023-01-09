package pl.edu.wat.wcy.epistimi.student.domain

class StudentBadRequestException(cause: String) : Exception(cause)
class StudentNotFoundException() : Exception("Student was not found")
