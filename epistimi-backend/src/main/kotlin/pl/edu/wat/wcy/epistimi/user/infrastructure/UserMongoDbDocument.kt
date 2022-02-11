package pl.edu.wat.wcy.epistimi.user.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.wat.wcy.epistimi.user.User

@Document(collection = "users")
data class UserMongoDbDocument(
    @Id val id: String? = null,
    val firstName: String,
    val lastName: String,
    val accountType: String,
)

fun UserMongoDbDocument.toDomain() = User(
    id = this.id!!,
    firstName = this.firstName,
    lastName = this.lastName,
    type = User.Type.valueOf(this.accountType)
)
