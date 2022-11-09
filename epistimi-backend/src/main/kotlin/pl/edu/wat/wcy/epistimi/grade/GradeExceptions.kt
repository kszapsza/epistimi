package pl.edu.wat.wcy.epistimi.grade

class GradeNotFoundException(id: GradeId) : Exception("Grade with id ${id.value} not found")
