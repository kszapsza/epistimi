package pl.edu.wat.wcy.epistimi.teacher

class TeacherNotFoundException(id: TeacherId) : Exception("Teacher with id ${id.value} was not found")
