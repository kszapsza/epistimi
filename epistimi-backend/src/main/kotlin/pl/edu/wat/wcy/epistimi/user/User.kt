package pl.edu.wat.wcy.epistimi.user

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: Role,
    val username: String,
    val passwordHash: String,
    val sex: Sex? = null,
) {
    enum class Role {
        EPISTIMI_ADMIN,
        ORGANIZATION_ADMIN,
        TEACHER,
        STUDENT,
        PARENT
    }

    enum class Sex {
        MALE, FEMALE, OTHER
    }
}
