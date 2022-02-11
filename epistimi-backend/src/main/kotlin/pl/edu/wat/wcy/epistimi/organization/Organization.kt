package pl.edu.wat.wcy.epistimi.organization

data class Organization(
    val id: String,
    val name: String,
    val adminId: String,
    val status: Status
) {
    enum class Status {
        ENABLED,
        DISABLED
    }
}
