package pl.edu.wat.wcy.epistimi.teacher.domain

import pl.edu.wat.wcy.epistimi.user.domain.UserId

class TeacherNotFoundException : Exception {
    constructor() : super("Teacher was not found")
    constructor(id: TeacherId) : super("Teacher with id [${id.value}] was not found")
}

class TeacherUserIdNotFoundException(id: UserId) : Exception("Teacher with user id [${id.value}] was not found")