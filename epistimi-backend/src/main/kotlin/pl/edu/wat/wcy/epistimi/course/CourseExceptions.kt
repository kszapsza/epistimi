package pl.edu.wat.wcy.epistimi.course

class CourseNotFoundException(id: CourseId) : Exception("Course with id ${id.value} not found")

