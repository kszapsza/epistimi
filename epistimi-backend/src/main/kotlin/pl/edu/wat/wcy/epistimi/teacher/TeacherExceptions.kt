package pl.edu.wat.wcy.epistimi.teacher

class TeacherNotFoundException : Exception {
    constructor() : super("Teacher was not found")
    constructor(id: TeacherId) : super("Teacher with id ${id.value} was not found")
}
