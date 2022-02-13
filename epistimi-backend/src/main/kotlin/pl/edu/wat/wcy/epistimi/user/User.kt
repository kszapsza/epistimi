package pl.edu.wat.wcy.epistimi.user

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val type: Type
) {
    enum class Type {
        EPISTIMI_ADMIN,
        ORGANIZATION_ADMIN,
        TEACHER,
        STUDENT,
        PARENT
    }
}
